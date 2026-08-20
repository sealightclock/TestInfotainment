package com.example.jonathan.testinfotainment.hvac.domain

import com.example.jonathan.testinfotainment.common.Constants
import kotlinx.coroutines.flow.Flow

class HvacUseCase(private val repository: HvacRepository) {
    fun getHvacState(): Flow<HvacEntity> = repository.getHvacState()

    suspend fun togglePower(currentState: HvacEntity): HvacEntity {
        val newState = currentState.copy(isPowerOn = !currentState.isPowerOn)
        repository.updateHvacState(newState)
        return newState
    }

    suspend fun adjustTemperature(currentState: HvacEntity, delta: Int): HvacEntity {
        if (!currentState.isPowerOn) return currentState
        val newTemp = (currentState.temperature + delta).coerceIn(Constants.TEMPERATURE_MIN, Constants.TEMPERATURE_MAX)
        val newState = currentState.copy(temperature = newTemp)
        repository.updateHvacState(newState)
        return newState
    }

    suspend fun adjustFanSpeed(currentState: HvacEntity, delta: Int): HvacEntity {
        if (!currentState.isPowerOn) return currentState
        if (currentState.fanSpeed == Constants.FAN_SPEED_MIN && delta < 0) {
            val newState = currentState.copy(isPowerOn = false)
            repository.updateHvacState(newState)
            return newState
        }
        val newFanSpeed = (currentState.fanSpeed + delta).coerceIn(Constants.FAN_SPEED_MIN, Constants.FAN_SPEED_MAX)
        val newState = currentState.copy(fanSpeed = newFanSpeed)
        repository.updateHvacState(newState)
        return newState
    }

    suspend fun toggleFrontDefroster(currentState: HvacEntity): HvacEntity {
        if (!currentState.isPowerOn) return currentState
        val turningOn = !currentState.isFrontDefrosterOn
        val newState = if (turningOn) {
            currentState.copy(
                isFrontDefrosterOn = true,
                temperature = Constants.TEMPERATURE_MAX,
                fanSpeed = Constants.FAN_SPEED_MAX
            )
        } else {
            val savedTemp = repository.getInt(HvacDataStoreDto.TEMPERATURE, Constants.TEMPERATURE_DEFAULT)
            val savedFan = repository.getInt(HvacDataStoreDto.FAN_SPEED, Constants.FAN_SPEED_MIN)
            currentState.copy(
                isFrontDefrosterOn = false,
                temperature = savedTemp,
                fanSpeed = savedFan
            )
        }
        repository.updateHvacState(newState)
        return newState
    }
}
