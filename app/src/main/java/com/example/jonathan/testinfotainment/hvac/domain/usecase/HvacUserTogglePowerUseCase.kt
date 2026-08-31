package com.example.jonathan.testinfotainment.hvac.domain.usecase

import com.example.jonathan.testinfotainment.hvac.domain.HvacEntity
import com.example.jonathan.testinfotainment.hvac.domain.HvacRepository

/**
 * Use case for toggling the main power state of the HVAC system.
 *
 * @property repository The HVAC repository for data access.
 * @property storeIsPowerOnToLocalUseCase Use case to persist the power state.
 */
class HvacUserTogglePowerUseCase(
    private val repository: HvacRepository,
    private val storeIsPowerOnToLocalUseCase: HvacStoreIsPowerOnToLocalUseCase
) {
    /**
     * Toggles the main power state of the HVAC system.
     *
     * @param currentState The current known state.
     * @return The updated HVAC state.
     */
    suspend operator fun invoke(currentState: HvacEntity): HvacEntity {
        val newState = currentState.copy(isPowerOn = !currentState.isPowerOn)
        repository.updatePlatformHvacState(newState)
        storeIsPowerOnToLocalUseCase(newState.isPowerOn)
        return newState
    }
}
