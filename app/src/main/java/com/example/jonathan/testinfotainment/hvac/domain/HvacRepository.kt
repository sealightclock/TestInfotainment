package com.example.jonathan.testinfotainment.hvac.domain

import kotlinx.coroutines.flow.Flow

/**
 * Interface for the HVAC repository.
 * Defines high-level operations for interacting with HVAC data.
 */
interface HvacRepository {
    /**
     * Returns a flow of the current HVAC state.
     */
    fun getHvacState(): Flow<HvacEntity>

    /**
     * Updates the HVAC state across both platform and local storage.
     *
     * @param hvacEntity The new state to apply.
     */
    suspend fun updateHvacState(hvacEntity: HvacEntity)
}
