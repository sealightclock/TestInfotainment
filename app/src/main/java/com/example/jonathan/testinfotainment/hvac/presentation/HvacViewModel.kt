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

class HvacViewModel(private val useCase: HvacUseCase) : ViewModel() {

    private val _state = MutableStateFlow(HvacState())
    val state: StateFlow<HvacState> = _state.asStateFlow()

    // Local source of truth for immediate UI updates
    private var currentEntity = HvacEntity()
    private var lastTemperatureUpdateTimestamp: Long = 0
    private var lastFanSpeedUpdateTimestamp: Long = 0

    init {
        viewModelScope.launch {
            // Use collectLatest to skip intermediate platform updates if they arrive rapidly.
            // This satisfies the 1s wait requirement while avoiding a backlog of stale updates.
            useCase.getHvacState().collectLatest { entity ->
                // [3] Presentation layer waits for 1 second after receiving value from Platform
                delay(1000)
                onIntent(HvacIntent.RefreshFromPlatform(entity))
            }
        }
    }

    fun onIntent(intent: HvacIntent) {
        viewModelScope.launch {
            val nextEntity: HvacEntity? = when (intent) {
                HvacIntent.TogglePower -> useCase.togglePower(currentEntity)
                HvacIntent.IncreaseTemperature -> {
                    lastTemperatureUpdateTimestamp = System.currentTimeMillis()
                    useCase.adjustTemperature(currentEntity, 1)
                }
                HvacIntent.DecreaseTemperature -> {
                    lastTemperatureUpdateTimestamp = System.currentTimeMillis()
                    useCase.adjustTemperature(currentEntity, -1)
                }
                HvacIntent.IncreaseFanSpeed -> {
                    lastFanSpeedUpdateTimestamp = System.currentTimeMillis()
                    useCase.adjustFanSpeed(currentEntity, 1)
                }
                HvacIntent.DecreaseFanSpeed -> {
                    lastFanSpeedUpdateTimestamp = System.currentTimeMillis()
                    useCase.adjustFanSpeed(currentEntity, -1)
                }
                HvacIntent.ToggleFrontDefroster -> {
                    lastTemperatureUpdateTimestamp = System.currentTimeMillis()
                    lastFanSpeedUpdateTimestamp = System.currentTimeMillis()
                    useCase.toggleFrontDefroster(currentEntity)
                }
                is HvacIntent.RefreshFromPlatform -> {
                    val currentTime = System.currentTimeMillis()
                    val platformEntity = intent.entity
                    
                    // Reject Platform values if the user modified them recently (within 2s threshold).
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
                    
                    val mergedEntity = platformEntity.copy(
                        temperature = mergedTemperature,
                        fanSpeed = mergedFanSpeed
                    )
                    
                    updateUi(mergedEntity)
                    null
                }
            }

            // [4] Update UI immediately for user-initiated changes
            nextEntity?.let { updateUi(it) }
        }
    }

    private fun updateUi(entity: HvacEntity) {
        currentEntity = entity
        _state.update {
            it.copy(
                isPowerOn = entity.isPowerOn,
                temperature = entity.temperature,
                fanSpeed = entity.fanSpeed,
                isFrontDefrosterOn = entity.isFrontDefrosterOn
            )
        }
    }
}
