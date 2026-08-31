package com.example.jonathan.testinfotainment.hvac.domain.usecase

import com.example.jonathan.testinfotainment.hvac.domain.HvacEntity
import com.example.jonathan.testinfotainment.hvac.domain.HvacRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for retrieving the current HVAC state from the platform.
 *
 * @property repository The HVAC repository for data access.
 */
class HvacGetStateUseCase(private val repository: HvacRepository) {
    /**
     * Returns a flow of the current HVAC state from the platform.
     */
    operator fun invoke(): Flow<HvacEntity> = repository.getHvacState()
}
