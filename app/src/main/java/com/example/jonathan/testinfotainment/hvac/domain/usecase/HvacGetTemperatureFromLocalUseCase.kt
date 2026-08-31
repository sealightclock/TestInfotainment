package com.example.jonathan.testinfotainment.hvac.domain.usecase

import com.example.jonathan.testinfotainment.hvac.domain.HvacRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Use case for retrieving the persisted Temperature from local storage.
 *
 * @property repository The HVAC repository for data access.
 */
class HvacGetTemperatureFromLocalUseCase(private val repository: HvacRepository) {
    /**
     * Returns a flow of the persisted Temperature.
     */
    operator fun invoke(): Flow<Int> = repository.getLocalHvacState().map { it.temperature }
}
