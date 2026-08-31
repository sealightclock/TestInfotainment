package com.example.jonathan.testinfotainment.hvac.domain.usecase

import com.example.jonathan.testinfotainment.hvac.domain.HvacRepository

/**
 * Use case for persisting the Fan Speed to local storage.
 *
 * @property repository The HVAC repository for data access.
 */
class HvacStoreFanSpeedToLocalUseCase(private val repository: HvacRepository) {
    /**
     * Persists the Fan Speed to local storage.
     *
     * @param fanSpeed The fan speed value to store.
     */
    suspend operator fun invoke(fanSpeed: Int) {
        repository.storeLocalFanSpeed(fanSpeed)
    }
}
