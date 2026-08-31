package com.example.jonathan.testinfotainment.hvac.domain.usecase

import com.example.jonathan.testinfotainment.hvac.domain.HvacRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Use case for retrieving the persisted Front Defroster state from local storage.
 *
 * @property repository The HVAC repository for data access.
 */
class HvacGetIsFrontDefrosterOnFromLocalUseCase(private val repository: HvacRepository) {
    /**
     * Returns a flow of the persisted Front Defroster state.
     */
    operator fun invoke(): Flow<Boolean> = repository.getLocalHvacState().map { it.isFrontDefrosterOn }
}
