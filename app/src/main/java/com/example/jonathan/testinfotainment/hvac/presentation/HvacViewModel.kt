package com.example.jonathan.testinfotainment.hvac.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jonathan.testinfotainment.common.Constants.DELAY_DATA_PLATFORM_TO_VIEWMODEL_TO_INTENT
import com.example.jonathan.testinfotainment.common.Constants.DELAY_VIEW_DISABLED_TO_ENABLED
import com.example.jonathan.testinfotainment.common.Constants.DELAY_DATA_CONCURRENCY_UI_TO_PLATFORM
import com.example.jonathan.testinfotainment.hvac.domain.HvacEntity
import com.example.jonathan.testinfotainment.hvac.domain.usecase.HvacGetStateUseCase
import com.example.jonathan.testinfotainment.hvac.domain.usecase.HvacUserAdjustFanSpeedUseCase
import com.example.jonathan.testinfotainment.hvac.domain.usecase.HvacUserAdjustTemperatureUseCase
import com.example.jonathan.testinfotainment.hvac.domain.usecase.HvacUserToggleFrontDefrosterUseCase
import com.example.jonathan.testinfotainment.hvac.domain.usecase.HvacUserTogglePowerUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

/**
 * ViewModel for the HVAC screen, managing state and user interactions.
 * Handles optimistic updates and merging data from the platform with a cooldown period.
 */
class HvacViewModel(
    private val hvacGetStateUseCase: HvacGetStateUseCase,
    private val hvacUserTogglePowerUseCase: HvacUserTogglePowerUseCase,
    private val hvacUserAdjustTemperatureUseCase: HvacUserAdjustTemperatureUseCase,
    private val hvacUserAdjustFanSpeedUseCase: HvacUserAdjustFanSpeedUseCase,
    private val hvacUserToggleFrontDefrosterUseCase: HvacUserToggleFrontDefrosterUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HvacState())
    val state: StateFlow<HvacState> = _state.asStateFlow()

    // Local source of truth for immediate UI updates and tracking recent user changes.
    private var currentEntity = HvacEntity()

    // These are for the purpose of ignoring Platform data if they arrive too  while the user is still interacting:
    private var lastPowerUpdateTimestamp: Long = 0
    private var lastTemperatureUpdateTimestamp: Long = 0
    private var lastFanSpeedUpdateTimestamp: Long = 0
    private var lastFrontDefrosterUpdateTimestamp: Long = 0

    init {
        viewModelScope.launch {
            // Observe HVAC state changes from the platform.
            // Use collectLatest to skip intermediate platform updates if they arrive rapidly.
            // This satisfies the 1s wait requirement while avoiding a backlog of stale updates.
            hvacGetStateUseCase().collectLatest { entity ->
                // [3] Presentation layer waits for 1 second after receiving value from Platform
                // to avoid immediate flickering if multiple updates arrive.
                delay(DELAY_DATA_PLATFORM_TO_VIEWMODEL_TO_INTENT)

                // Divide the platform update into granular intents for better efficiency.
                // This ensures we only process properties that might have changed.
                onIntent(HvacIntent.PlatformRefreshPowerIntent(entity.isPowerOn))
                onIntent(HvacIntent.PlatformRefreshTemperatureIntent(entity.temperature))
                onIntent(HvacIntent.PlatformRefreshFanSpeedIntent(entity.fanSpeed))
                onIntent(HvacIntent.PlatformRefreshFrontDefrosterIntent(entity.isFrontDefrosterOn))
            }
        }
    }

    /**
     * Processes user intents and platform updates to drive the UI state.
     * Special handling: Implements optimistic updates for power and defroster,
     * and a 2-second cooldown period where platform values are ignored for recently changed properties.
     *
     * @param intent The HVAC-related intent to process.
     */
    fun onIntent(intent: HvacIntent) {
        viewModelScope.launch {
            // In the "when" block:
            //   [1] do not use keyword "is" if there is no argument list, for efficiency.
            //   [2] use keyword "is" if there is argument list, for type matching and smart
            //   casting.
            val nextEntity: HvacEntity? = when (intent) {
                // User Intent to toggle Power:
                HvacIntent.UserTogglePowerIntent -> {
                    val entityBeforeChange = currentEntity
                    lastPowerUpdateTimestamp = System.currentTimeMillis()
                    
                    // Disable the button temporarily to prevent rapid multi-taps.
                    _state.update { it.copy(isPowerButtonEnabled = false) }
                    viewModelScope.launch {
                        delay(DELAY_VIEW_DISABLED_TO_ENABLED)
                        _state.update { it.copy(isPowerButtonEnabled = true) }
                    }
                    
                    // Optimistic update: Update UI immediately before waiting for platform confirmation.
                    updateUi(entityBeforeChange.copy(isPowerOn = !entityBeforeChange.isPowerOn))
                    hvacUserTogglePowerUseCase(entityBeforeChange)
                }

                // User Intent to increase Temperature:
                HvacIntent.UserIncreaseTemperatureIntent -> {
                    // Temperature cannot be adjusted if front defroster is on (it's maxed).
                    if (!currentEntity.isFrontDefrosterOn) {
                        lastTemperatureUpdateTimestamp = System.currentTimeMillis()
                        hvacUserAdjustTemperatureUseCase(currentEntity, 1)
                    } else null
                }

                // User Intent to decrease Temperature:
                HvacIntent.UserDecreaseTemperatureIntent -> {
                    // Temperature cannot be adjusted if front defroster is on (it's maxed).
                    if (!currentEntity.isFrontDefrosterOn) {
                        lastTemperatureUpdateTimestamp = System.currentTimeMillis()
                        hvacUserAdjustTemperatureUseCase(currentEntity, -1)
                    } else null
                }

                // User Intent to increase Fan Speed:
                HvacIntent.UserIncreaseFanSpeedIntent -> {
                    // Fan speed cannot be adjusted if front defroster is on (it's maxed).
                    if (!currentEntity.isFrontDefrosterOn) {
                        lastFanSpeedUpdateTimestamp = System.currentTimeMillis()
                        hvacUserAdjustFanSpeedUseCase(currentEntity, 1)
                    } else null
                }

                // User Intent to decrease Fan Speed:
                HvacIntent.UserDecreaseFanSpeedIntent -> {
                    // Fan speed cannot be adjusted if front defroster is on (it's maxed).
                    if (!currentEntity.isFrontDefrosterOn) {
                        lastFanSpeedUpdateTimestamp = System.currentTimeMillis()
                        hvacUserAdjustFanSpeedUseCase(currentEntity, -1)
                    } else null
                }

                // User Intent to toggle Front Defroster:
                HvacIntent.UserToggleFrontDefrosterIntent -> {
                    val entityBeforeChange = currentEntity
                    val currentTime = System.currentTimeMillis()
                    
                    // Update all related timestamps since defroster overrides temp and fan speed.
                    lastFrontDefrosterUpdateTimestamp = currentTime
                    lastTemperatureUpdateTimestamp = currentTime
                    lastFanSpeedUpdateTimestamp = currentTime
                    
                    // Disable button temporarily to prevent spamming.
                    _state.update { it.copy(isFrontDefrosterButtonEnabled = false) }
                    viewModelScope.launch {
                        delay(DELAY_VIEW_DISABLED_TO_ENABLED)
                        _state.update { it.copy(isFrontDefrosterButtonEnabled = true) }
                    }
                    
                    // Optimistic update - just toggle the boolean, HvacState handles the value overrides.
                    updateUi(entityBeforeChange.copy(isFrontDefrosterOn = !entityBeforeChange.isFrontDefrosterOn))
                    hvacUserToggleFrontDefrosterUseCase(entityBeforeChange)
                }

                // Platform Intents to refresh specific properties:
                // Cooldown logic: Reject Platform values if the user modified them recently (within 2s threshold).
                // This prevents "jumping" sliders or values when the platform hasn't yet processed the user's change.

                is HvacIntent.PlatformRefreshPowerIntent -> {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastPowerUpdateTimestamp >= DELAY_DATA_CONCURRENCY_UI_TO_PLATFORM) {
                        updateUi(currentEntity.copy(isPowerOn = intent.isPowerOn))
                    }
                    null
                }

                is HvacIntent.PlatformRefreshTemperatureIntent -> {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastTemperatureUpdateTimestamp >= DELAY_DATA_CONCURRENCY_UI_TO_PLATFORM) {
                        updateUi(currentEntity.copy(temperature = intent.temperature))
                    }
                    null
                }

                is HvacIntent.PlatformRefreshFanSpeedIntent -> {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastFanSpeedUpdateTimestamp >= DELAY_DATA_CONCURRENCY_UI_TO_PLATFORM) {
                        updateUi(currentEntity.copy(fanSpeed = intent.fanSpeed))
                    }
                    null
                }

                is HvacIntent.PlatformRefreshFrontDefrosterIntent -> {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastFrontDefrosterUpdateTimestamp >= DELAY_DATA_CONCURRENCY_UI_TO_PLATFORM) {
                        updateUi(currentEntity.copy(isFrontDefrosterOn = intent.isFrontDefrosterOn))
                    }
                    null
                }
            }

            // [4] Update UI immediately for user-initiated changes if a nextEntity was calculated.
            nextEntity?.let { updateUi(it) }
        }
    }

    /**
     * Updates the internal local state and pushes a new HvacState to the UI flow.
     */
    private fun updateUi(entity: HvacEntity) {
        currentEntity = entity
        _state.update {
            it.copy(hvac = entity)
        }
    }
}
