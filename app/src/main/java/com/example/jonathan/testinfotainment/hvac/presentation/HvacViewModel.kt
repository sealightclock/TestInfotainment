package com.example.jonathan.testinfotainment.hvac.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jonathan.testinfotainment.common.Constants
import com.example.jonathan.testinfotainment.common.Constants.DELAY_DATA_PLATFORM_TO_VIEWMODEL_TO_INTENT
import com.example.jonathan.testinfotainment.common.Constants.DELAY_VIEW_DISABLED_TO_ENABLED
import com.example.jonathan.testinfotainment.common.Constants.DELAY_DATA_CONCURRENCY_UI_TO_PLATFORM
import com.example.jonathan.testinfotainment.hvac.domain.HvacEntity
import com.example.jonathan.testinfotainment.hvac.domain.usecase.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

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

    // These are for the purpose of ignoring Platform data if they arrive too soon while the user is still interacting:
    private var lastPowerUpdateTimestamp: Long = 0
    private var lastTemperatureUpdateTimestamp: Long = 0
    private var lastFanSpeedUpdateTimestamp: Long = 0
    private var lastFrontDefrosterUpdateTimestamp: Long = 0

    // Intent queue to ensure sequential processing and avoid race conditions.
    private val intentChannel = Channel<HvacIntent>(Channel.UNLIMITED)

    init {
        // Process intents sequentially from the channel.
        viewModelScope.launch {
            intentChannel.receiveAsFlow().collect { intent ->
                handleIntent(intent)
            }
        }

        viewModelScope.launch {
            // Observe HVAC state changes from the platform.
            hvacGetStateUseCase().collectLatest { entity ->
                // Wait for the simulated round-trip delay.
                delay(DELAY_DATA_PLATFORM_TO_VIEWMODEL_TO_INTENT)

                // Consolidate platform updates into a single intent.
                onIntent(HvacIntent.PlatformRefreshStateIntent(entity))
            }
        }
    }

    /**
     * Public API to push intents to the ViewModel.
     */
    fun onIntent(intent: HvacIntent) {
        intentChannel.trySend(intent)
    }

    /**
     * Processes user intents and platform updates to drive the UI state.
     * Special handling: Implements optimistic updates for power and defroster,
     * and a 2-second cooldown period where platform values are ignored for recently changed properties.
     */
    private suspend fun handleIntent(intent: HvacIntent) {
        val nextEntity: HvacEntity? = when (intent) {
            HvacIntent.UserTogglePowerIntent -> {
                val entityBeforeChange = currentEntity
                lastPowerUpdateTimestamp = System.currentTimeMillis()
                
                // Disable button and update UI optimistically.
                _state.update { it.copy(isPowerButtonEnabled = false) }
                viewModelScope.launch {
                    delay(DELAY_VIEW_DISABLED_TO_ENABLED)
                    _state.update { it.copy(isPowerButtonEnabled = true) }
                }
                
                updateUi(entityBeforeChange.copy(isPowerOn = !entityBeforeChange.isPowerOn))
                hvacUserTogglePowerUseCase(entityBeforeChange)
            }

            HvacIntent.UserIncreaseTemperatureIntent -> {
                if (!currentEntity.isFrontDefrosterOn) {
                    lastTemperatureUpdateTimestamp = System.currentTimeMillis()
                    hvacUserAdjustTemperatureUseCase(currentEntity, 1)
                } else null
            }

            HvacIntent.UserDecreaseTemperatureIntent -> {
                if (!currentEntity.isFrontDefrosterOn) {
                    lastTemperatureUpdateTimestamp = System.currentTimeMillis()
                    hvacUserAdjustTemperatureUseCase(currentEntity, -1)
                } else null
            }

            HvacIntent.UserIncreaseFanSpeedIntent -> {
                if (!currentEntity.isFrontDefrosterOn) {
                    lastFanSpeedUpdateTimestamp = System.currentTimeMillis()
                    hvacUserAdjustFanSpeedUseCase(currentEntity, 1)
                } else null
            }

            HvacIntent.UserDecreaseFanSpeedIntent -> {
                if (!currentEntity.isFrontDefrosterOn) {
                    lastFanSpeedUpdateTimestamp = System.currentTimeMillis()
                    hvacUserAdjustFanSpeedUseCase(currentEntity, -1)
                } else null
            }

            HvacIntent.UserToggleFrontDefrosterIntent -> {
                val entityBeforeChange = currentEntity
                val currentTime = System.currentTimeMillis()
                
                lastFrontDefrosterUpdateTimestamp = currentTime
                lastTemperatureUpdateTimestamp = currentTime
                lastFanSpeedUpdateTimestamp = currentTime
                
                _state.update { it.copy(isFrontDefrosterButtonEnabled = false) }
                viewModelScope.launch {
                    delay(DELAY_VIEW_DISABLED_TO_ENABLED)
                    _state.update { it.copy(isFrontDefrosterButtonEnabled = true) }
                }
                
                updateUi(entityBeforeChange.copy(isFrontDefrosterOn = !entityBeforeChange.isFrontDefrosterOn))
                hvacUserToggleFrontDefrosterUseCase(entityBeforeChange)
            }

            is HvacIntent.PlatformRefreshStateIntent -> {
                val currentTime = System.currentTimeMillis()
                var mergedEntity = currentEntity
                
                // Merge platform state with local state based on cooldown timestamps.
                if ((currentTime - lastPowerUpdateTimestamp) >= DELAY_DATA_CONCURRENCY_UI_TO_PLATFORM) {
                    mergedEntity = mergedEntity.copy(isPowerOn = intent.entity.isPowerOn)
                }
                if ((currentTime - lastTemperatureUpdateTimestamp) >= DELAY_DATA_CONCURRENCY_UI_TO_PLATFORM) {
                    mergedEntity = mergedEntity.copy(temperature = intent.entity.temperature)
                }
                if ((currentTime - lastFanSpeedUpdateTimestamp) >= DELAY_DATA_CONCURRENCY_UI_TO_PLATFORM) {
                    mergedEntity = mergedEntity.copy(fanSpeed = intent.entity.fanSpeed)
                }
                if ((currentTime - lastFrontDefrosterUpdateTimestamp) >= DELAY_DATA_CONCURRENCY_UI_TO_PLATFORM) {
                    mergedEntity = mergedEntity.copy(isFrontDefrosterOn = intent.entity.isFrontDefrosterOn)
                }
                
                updateUi(mergedEntity)
                null
            }
        }

        // Apply final state update for user intents.
        nextEntity?.let { updateUi(it) }
    }

    /**
     * Updates the internal local state and pushes a new HvacState to the UI flow.
     */
    private fun updateUi(entity: HvacEntity) {
        currentEntity = entity
        _state.update {
            it.copy(
                isPowerOn = entity.isPowerOn,
                // Display overrides for front defroster (temperature and fan speed are maxed)
                temperature = if (entity.isFrontDefrosterOn) Constants.TEMPERATURE_MAX else entity.temperature,
                fanSpeed = if (entity.isFrontDefrosterOn) Constants.FAN_SPEED_MAX else entity.fanSpeed,
                isFrontDefrosterOn = entity.isFrontDefrosterOn
            )
        }
    }
}
