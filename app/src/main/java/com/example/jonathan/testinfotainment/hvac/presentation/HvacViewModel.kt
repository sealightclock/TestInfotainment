package com.example.jonathan.testinfotainment.hvac.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jonathan.testinfotainment.hvac.domain.HvacEntity
import com.example.jonathan.testinfotainment.hvac.domain.HvacUseCase
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
 *
 * @property useCase The business logic provider for HVAC operations.
 */
class HvacViewModel(private val useCase: HvacUseCase) : ViewModel() {

    private val _state = MutableStateFlow(HvacState())
    val state: StateFlow<HvacState> = _state.asStateFlow()

    // Local source of truth for immediate UI updates and tracking recent user changes.
    private var currentEntity = HvacEntity()
    private var lastTemperatureUpdateTimestamp: Long = 0
    private var lastFanSpeedUpdateTimestamp: Long = 0
    private var lastPowerUpdateTimestamp: Long = 0
    private var lastFrontDefrosterUpdateTimestamp: Long = 0

    init {
        viewModelScope.launch {
            // Observe HVAC state changes from the platform.
            // Use collectLatest to skip intermediate platform updates if they arrive rapidly.
            // This satisfies the 1s wait requirement while avoiding a backlog of stale updates.
            useCase.getHvacState().collectLatest { entity ->
                // [3] Presentation layer waits for 1 second after receiving value from Platform
                // to avoid immediate flickering if multiple updates arrive.
                delay(1000)
                onIntent(HvacIntent.RefreshFromPlatform(entity))
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
            val nextEntity: HvacEntity? = when (intent) {
                HvacIntent.TogglePower -> {
                    val entityBeforeChange = currentEntity
                    lastPowerUpdateTimestamp = System.currentTimeMillis()
                    
                    // Disable the button temporarily to prevent rapid multi-taps.
                    _state.update { it.copy(isPowerButtonEnabled = false) }
                    viewModelScope.launch {
                        delay(2000)
                        _state.update { it.copy(isPowerButtonEnabled = true) }
                    }
                    
                    // Optimistic update: Update UI immediately before waiting for platform confirmation.
                    updateUi(entityBeforeChange.copy(isPowerOn = !entityBeforeChange.isPowerOn))
                    useCase.togglePower(entityBeforeChange)
                }
                HvacIntent.IncreaseTemperature -> {
                    // Temperature cannot be adjusted if front defroster is on (it's maxed).
                    if (!currentEntity.isFrontDefrosterOn) {
                        lastTemperatureUpdateTimestamp = System.currentTimeMillis()
                        useCase.adjustTemperature(currentEntity, 1)
                    } else null
                }
                HvacIntent.DecreaseTemperature -> {
                    // Temperature cannot be adjusted if front defroster is on (it's maxed).
                    if (!currentEntity.isFrontDefrosterOn) {
                        lastTemperatureUpdateTimestamp = System.currentTimeMillis()
                        useCase.adjustTemperature(currentEntity, -1)
                    } else null
                }
                HvacIntent.IncreaseFanSpeed -> {
                    // Fan speed cannot be adjusted if front defroster is on (it's maxed).
                    if (!currentEntity.isFrontDefrosterOn) {
                        lastFanSpeedUpdateTimestamp = System.currentTimeMillis()
                        useCase.adjustFanSpeed(currentEntity, 1)
                    } else null
                }
                HvacIntent.DecreaseFanSpeed -> {
                    // Fan speed cannot be adjusted if front defroster is on (it's maxed).
                    if (!currentEntity.isFrontDefrosterOn) {
                        lastFanSpeedUpdateTimestamp = System.currentTimeMillis()
                        useCase.adjustFanSpeed(currentEntity, -1)
                    } else null
                }
                HvacIntent.ToggleFrontDefroster -> {
                    val entityBeforeChange = currentEntity
                    val currentTime = System.currentTimeMillis()
                    
                    // Update all related timestamps since defroster overrides temp and fan speed.
                    lastFrontDefrosterUpdateTimestamp = currentTime
                    lastTemperatureUpdateTimestamp = currentTime
                    lastFanSpeedUpdateTimestamp = currentTime
                    
                    // Disable button temporarily to prevent spamming.
                    _state.update { it.copy(isFrontDefrosterButtonEnabled = false) }
                    viewModelScope.launch {
                        delay(2000)
                        _state.update { it.copy(isFrontDefrosterButtonEnabled = true) }
                    }
                    
                    // Optimistic update - just toggle the boolean, HvacState handles the value overrides.
                    updateUi(entityBeforeChange.copy(isFrontDefrosterOn = !entityBeforeChange.isFrontDefrosterOn))
                    useCase.toggleFrontDefroster(entityBeforeChange)
                }
                is HvacIntent.RefreshFromPlatform -> {
                    val currentTime = System.currentTimeMillis()
                    val platformEntity = intent.entity
                    
                    // Cooldown logic: Reject Platform values if the user modified them recently (within 2s threshold).
                    // This prevents "jumping" sliders or values when the platform hasn't yet processed the user's change.
                    
                    val mergedPower = if (currentTime - lastPowerUpdateTimestamp < 2000) {
                        currentEntity.isPowerOn
                    } else {
                        platformEntity.isPowerOn
                    }

                    val mergedTemperature = if (currentTime - lastTemperatureUpdateTimestamp < 2000) {
                        currentEntity.temperature
                    } else {
                        platformEntity.temperature
                    }

                    val mergedFanSpeed = if (currentTime - lastFanSpeedUpdateTimestamp < 2000) {
                        currentEntity.fanSpeed
                    } else {
                        platformEntity.fanSpeed
                    }

                    val mergedFrontDefroster = if (currentTime - lastFrontDefrosterUpdateTimestamp < 2000) {
                        currentEntity.isFrontDefrosterOn
                    } else {
                        platformEntity.isFrontDefrosterOn
                    }
                    
                    val mergedEntity = platformEntity.copy(
                        isPowerOn = mergedPower,
                        temperature = mergedTemperature,
                        fanSpeed = mergedFanSpeed,
                        isFrontDefrosterOn = mergedFrontDefroster
                    )
                    
                    updateUi(mergedEntity)
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
