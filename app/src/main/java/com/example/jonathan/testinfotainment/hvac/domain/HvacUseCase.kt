package com.example.jonathan.testinfotainment.hvac.domain

import kotlinx.coroutines.flow.Flow

class HvacUseCase(private val repository: HvacRepository) {
    fun getHvacState(): Flow<HvacEntity> = repository.getHvacState()

    suspend fun togglePower(currentState: HvacEntity) {
        val newState = currentState.copy(isPowerOn = !currentState.isPowerOn)
        repository.updateHvacState(newState)
    }

    suspend fun adjustTemperature(currentState: HvacEntity, delta: Int) {
        if (!currentState.isPowerOn) return
        val newTemp = (currentState.temperature + delta).coerceIn(63, 91)
        repository.updateHvacState(currentState.copy(temperature = newTemp))
    }

    suspend fun adjustFanSpeed(currentState: HvacEntity, delta: Int) {
        if (!currentState.isPowerOn) return
        val newFanSpeed = (currentState.fanSpeed + delta).coerceIn(0, 7)
        if (newFanSpeed == 0) {
            repository.updateHvacState(currentState.copy(fanSpeed = 0, isPowerOn = false))
        } else {
            repository.updateHvacState(currentState.copy(fanSpeed = newFanSpeed))
        }
    }

    suspend fun toggleFrontDefroster(currentState: HvacEntity) {
        if (!currentState.isPowerOn) return
        val isDefrosterOn = !currentState.isFrontDefrosterOn
        if (isDefrosterOn) {
            repository.updateHvacState(
                currentState.copy(
                    isFrontDefrosterOn = true,
                    temperature = 91,
                    fanSpeed = 7
                )
            )
        } else {
            repository.updateHvacState(currentState.copy(isFrontDefrosterOn = false))
        }
    }
}
