package com.example.jonathan.testinfotainment.hvac.domain.usecase

import com.example.jonathan.testinfotainment.hvac.domain.HvacRepository

/**
 * Use case for persisting the Front Defroster state to local storage.
 *
 * @property repository The HVAC repository for data access.
 */
class HvacStoreIsFrontDefrosterOnToLocalUseCase(private val repository: HvacRepository) {
    /**
     * Persists the Front Defroster state to local storage.
     *
     * @param isOn The Front Defroster state to store.
     */
    suspend operator fun invoke(isOn: Boolean) {
        repository.storeLocalIsFrontDefrosterOn(isOn)
    }
}
