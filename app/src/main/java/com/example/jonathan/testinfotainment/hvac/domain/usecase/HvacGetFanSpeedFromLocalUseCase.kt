package com.example.jonathan.testinfotainment.hvac.domain.usecase

import com.example.jonathan.testinfotainment.hvac.domain.HvacRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Use case for retrieving the persisted Fan Speed from local storage.
 *
 * @property repository The HVAC repository for data access.
 */
class HvacGetFanSpeedFromLocalUseCase(private val repository: HvacRepository) {
    /**
     * Returns a flow of the persisted Fan Speed.
     */
    operator fun invoke(): Flow<Int> = repository.getLocalHvacState().map { it.fanSpeed }
}
