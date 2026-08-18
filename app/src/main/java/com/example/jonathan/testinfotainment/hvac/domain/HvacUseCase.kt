package com.example.jonathan.testinfotainment.hvac.domain

import com.example.jonathan.testinfotainment.common.Constants
import kotlinx.coroutines.flow.Flow

class HvacUseCase(private val repository: HvacRepository) {
    fun getHvacState(): Flow<HvacEntity> = repository.getHvacState()

    suspend fun togglePower(currentState: HvacEntity) {
        val newState = currentState.copy(isPowerOn = !currentState.isPowerOn)
        repository.updateHvacState(newState)
    }

    suspend fun adjustTemperature(currentState: HvacEntity, delta: Int) {
        if (!currentState.isPowerOn) return
        val newTemp = (currentState.temperature + delta).coerceIn(Constants.TEMPERATURE_MIN, Constants.TEMPERATURE_MAX)
        repository.updateHvacState(currentState.copy(temperature = newTemp))
    }

    suspend fun adjustFanSpeed(currentState: HvacEntity, delta: Int) {
        if (!currentState.isPowerOn) return
        if (currentState.fanSpeed == Constants.FAN_SPEED_MIN && delta < 0) {
            repository.updateHvacState(currentState.copy(isPowerOn = false))
            return
        }
        val newFanSpeed = (currentState.fanSpeed + delta).coerceIn(Constants.FAN_SPEED_MIN, Constants.FAN_SPEED_MAX)
        repository.updateHvacState(currentState.copy(fanSpeed = newFanSpeed))
    }

    suspend fun toggleFrontDefroster(currentState: HvacEntity) {
        if (!currentState.isPowerOn) return
        val isDefrosterOn = !currentState.isFrontDefrosterOn
        if (isDefrosterOn) {
            // Turning ON: Save current values then set to MAX
            repository.updateHvacState(
                currentState.copy(
                    isFrontDefrosterOn = true,
                    temperature = Constants.TEMPERATURE_MAX,
                    fanSpeed = Constants.FAN_SPEED_MAX,

                    savedTemperature = currentState.temperature,
                    savedFanSpeed = currentState.fanSpeed
                )
            )
        } else {
            // Turning OFF: Restore from saved values
            repository.updateHvacState(
                currentState.copy(
                    isFrontDefrosterOn = false,

                    temperature = currentState.savedTemperature,
                    fanSpeed = currentState.savedFanSpeed
                )
            )
        }
    }
}
