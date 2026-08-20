package com.example.jonathan.testinfotainment.hvac.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jonathan.testinfotainment.hvac.domain.HvacEntity
import com.example.jonathan.testinfotainment.hvac.domain.HvacUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class HvacViewModel(private val useCase: HvacUseCase) : ViewModel() {

    private val _state = MutableStateFlow(HvacState())
    val state: StateFlow<HvacState> = _state.asStateFlow()

    // Local source of truth for immediate UI updates
    private var currentEntity = HvacEntity()

    init {
        viewModelScope.launch {
            useCase.getHvacState().collect { entity ->
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
                HvacIntent.IncreaseTemperature -> useCase.adjustTemperature(currentEntity, 1)
                HvacIntent.DecreaseTemperature -> useCase.adjustTemperature(currentEntity, -1)
                HvacIntent.IncreaseFanSpeed -> useCase.adjustFanSpeed(currentEntity, 1)
                HvacIntent.DecreaseFanSpeed -> useCase.adjustFanSpeed(currentEntity, -1)
                HvacIntent.ToggleFrontDefroster -> useCase.toggleFrontDefroster(currentEntity)
                is HvacIntent.RefreshFromPlatform -> {
                    updateUi(intent.entity)
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
