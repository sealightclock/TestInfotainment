package com.example.jonathan.testinfotainment.hvac.domain.usecase

import com.example.jonathan.testinfotainment.hvac.domain.HvacRepository

/**
 * Use case for persisting the Temperature to local storage.
 *
 * @property repository The HVAC repository for data access.
 */
class HvacStoreTemperatureToLocalUseCase(private val repository: HvacRepository) {
    /**
     * Persists the Temperature to local storage.
     *
     * @param temperature The temperature value to store.
     */
    suspend operator fun invoke(temperature: Int) {
        repository.storeLocalTemperature(temperature)
    }
}
