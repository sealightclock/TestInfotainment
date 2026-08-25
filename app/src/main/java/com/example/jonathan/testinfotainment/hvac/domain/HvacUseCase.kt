package com.example.jonathan.testinfotainment.hvac.domain

import com.example.jonathan.testinfotainment.common.Constants
import kotlinx.coroutines.flow.Flow

/**
 * Business logic layer for HVAC operations.
 * Coordinates between the UI (ViewModel) and the data repository.
 *
 * @property repository The HVAC repository for data access.
 */
class HvacUseCase(private val repository: HvacRepository) {
    /**
     * Returns a flow of the current HVAC state from the platform.
     */
    fun getHvacState(): Flow<HvacEntity> = repository.getHvacState()

    /**
     * Toggles the main power state of the HVAC system.
     *
     * @param currentState The current known state.
     * @return The updated HVAC state.
     */
    suspend fun togglePower(currentState: HvacEntity): HvacEntity {
        val newState = currentState.copy(isPowerOn = !currentState.isPowerOn)
        repository.updateHvacState(newState)
        return newState
    }

    /**
     * Adjusts the temperature by a given delta.
     * Special handling: If power is off, no adjustment is made.
     *
     * @param currentState The current known state.
     * @param delta The amount to change the temperature by.
     * @return The updated HVAC state.
     */
    suspend fun adjustTemperature(currentState: HvacEntity, delta: Int): HvacEntity {
        if (!currentState.isPowerOn) return currentState
        val newTemp = (currentState.temperature + delta).coerceIn(Constants.TEMPERATURE_MIN, Constants.TEMPERATURE_MAX)
        val newState = currentState.copy(temperature = newTemp)
        repository.updateHvacState(newState)
        return newState
    }

    /**
     * Adjusts the fan speed by a given delta.
     * Special handling:
     * 1. If power is off, no adjustment is made.
     * 2. If fan speed is at minimum and user tries to decrease it, the HVAC system powers off.
     *
     * @param currentState The current known state.
     * @param delta The amount to change the fan speed by.
     * @return The updated HVAC state.
     */
    suspend fun adjustFanSpeed(currentState: HvacEntity, delta: Int): HvacEntity {
        if (!currentState.isPowerOn) return currentState
        
        // Special case: decreasing fan speed from MIN turns off the whole system.
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

    /**
     * Toggles the front defroster state.
     * Special handling: If power is off, no adjustment is made.
     *
     * @param currentState The current known state.
     * @return The updated HVAC state.
     */
    suspend fun toggleFrontDefroster(currentState: HvacEntity): HvacEntity {
        if (!currentState.isPowerOn) return currentState
        val newState = currentState.copy(isFrontDefrosterOn = !currentState.isFrontDefrosterOn)
        repository.updateHvacState(newState)
        return newState
    }
}
