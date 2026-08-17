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

class HvacViewModel(private val useCase: HvacUseCase) : ViewModel() {

    private val _state = MutableStateFlow(HvacState())
    val state: StateFlow<HvacState> = _state.asStateFlow()

    private var currentEntity = HvacEntity()

    init {
        viewModelScope.launch {
            useCase.getHvacState().collect { entity ->
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
    }

    fun onIntent(intent: HvacIntent) {
        viewModelScope.launch {
            when (intent) {
                HvacIntent.TogglePower -> useCase.togglePower(currentEntity)
                HvacIntent.IncreaseTemperature -> useCase.adjustTemperature(currentEntity, 1)
                HvacIntent.DecreaseTemperature -> useCase.adjustTemperature(currentEntity, -1)
                HvacIntent.IncreaseFanSpeed -> useCase.adjustFanSpeed(currentEntity, 1)
                HvacIntent.DecreaseFanSpeed -> useCase.adjustFanSpeed(currentEntity, -1)
                HvacIntent.ToggleFrontDefroster -> useCase.toggleFrontDefroster(currentEntity)
            }
        }
    }
}
