package com.example.jonathan.testinfotainment.hvac.domain.usecase

import com.example.jonathan.testinfotainment.hvac.domain.HvacEntity
import com.example.jonathan.testinfotainment.hvac.domain.HvacRepository

/**
 * Use case for toggling the front defroster state.
 *
 * @property repository The HVAC repository for data access.
 */
class HvacUserToggleFrontDefrosterUseCase(private val repository: HvacRepository) {
    /**
     * Toggles the front defroster state.
     * Special handling: If power is off, no adjustment is made.
     *
     * @param currentState The current known state.
     * @return The updated HVAC state.
     */
    suspend operator fun invoke(currentState: HvacEntity): HvacEntity {
        if (!currentState.isPowerOn) return currentState
        val newState = currentState.copy(isFrontDefrosterOn = !currentState.isFrontDefrosterOn)
        repository.updateHvacState(newState)
        return newState
    }
}
