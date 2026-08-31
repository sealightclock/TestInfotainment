package com.example.jonathan.testinfotainment.hvac.domain.usecase

import com.example.jonathan.testinfotainment.common.Constants
import com.example.jonathan.testinfotainment.hvac.domain.HvacEntity
import com.example.jonathan.testinfotainment.hvac.domain.HvacRepository

/**
 * Use case for adjusting the fan speed by a given delta.
 *
 * @property repository The HVAC repository for data access.
 * @property storeFanSpeedToLocalUseCase Use case to persist the fan speed.
 * @property storeIsPowerOnToLocalUseCase Use case to persist power state (for auto-off).
 */
class HvacUserAdjustFanSpeedUseCase(
    private val repository: HvacRepository,
    private val storeFanSpeedToLocalUseCase: HvacStoreFanSpeedToLocalUseCase,
    private val storeIsPowerOnToLocalUseCase: HvacStoreIsPowerOnToLocalUseCase
) {
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
    suspend operator fun invoke(currentState: HvacEntity, delta: Int): HvacEntity {
        if (!currentState.isPowerOn) return currentState
        
        // Special case: decreasing fan speed from MIN turns off the whole system.
        if (currentState.fanSpeed == Constants.FAN_SPEED_MIN && delta < 0) {
            val newState = currentState.copy(isPowerOn = false)
            repository.updatePlatformHvacState(newState)
            storeIsPowerOnToLocalUseCase(false)
            return newState
        }
        
        val newFanSpeed = (currentState.fanSpeed + delta).coerceIn(Constants.FAN_SPEED_MIN, Constants.FAN_SPEED_MAX)
        val newState = currentState.copy(fanSpeed = newFanSpeed)
        repository.updatePlatformHvacState(newState)
        storeFanSpeedToLocalUseCase(newFanSpeed)
        return newState
    }
}
