package com.example.jonathan.testinfotainment.hvac.domain.usecase

import com.example.jonathan.testinfotainment.hvac.domain.HvacRepository

/**
 * Use case for persisting the Power state to local storage.
 *
 * @property repository The HVAC repository for data access.
 */
class HvacStoreIsPowerOnToLocalUseCase(private val repository: HvacRepository) {
    /**
     * Persists the Power state to local storage.
     *
     * @param isPowerOn The Power state to store.
     */
    suspend operator fun invoke(isPowerOn: Boolean) {
        repository.storeLocalIsPowerOn(isPowerOn)
    }
}
