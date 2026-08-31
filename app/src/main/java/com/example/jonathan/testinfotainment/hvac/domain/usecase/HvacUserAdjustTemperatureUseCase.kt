package com.example.jonathan.testinfotainment.hvac.domain.usecase

import com.example.jonathan.testinfotainment.common.Constants
import com.example.jonathan.testinfotainment.hvac.domain.HvacEntity
import com.example.jonathan.testinfotainment.hvac.domain.HvacRepository

/**
 * Use case for adjusting the temperature by a given delta.
 *
 * @property repository The HVAC repository for data access.
 */
class HvacUserAdjustTemperatureUseCase(private val repository: HvacRepository) {
    /**
     * Adjusts the temperature by a given delta.
     * Special handling: If power is off, no adjustment is made.
     *
     * @param currentState The current known state.
     * @param delta The amount to change the temperature by.
     * @return The updated HVAC state.
     */
    suspend operator fun invoke(currentState: HvacEntity, delta: Int): HvacEntity {
        if (!currentState.isPowerOn) return currentState
        val newTemp = (currentState.temperature + delta).coerceIn(Constants.TEMPERATURE_MIN, Constants.TEMPERATURE_MAX)
        val newState = currentState.copy(temperature = newTemp)
        repository.updateHvacState(newState)
        return newState
    }
}
