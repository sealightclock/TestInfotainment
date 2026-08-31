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
     * Updates the HVAC state on the vehicle platform.
     *
     * @param hvacEntity The new state to apply.
     */
    suspend fun updatePlatformHvacState(hvacEntity: HvacEntity)

    /**
     * Returns a flow of the current HVAC state stored locally.
     */
    fun getLocalHvacState(): Flow<HvacEntity>

    /**
     * Updates only the local HVAC state.
     *
     * @param hvacEntity The state to persist locally.
     */
    suspend fun updateLocalHvacState(hvacEntity: HvacEntity)

    suspend fun storeLocalIsPowerOn(isPowerOn: Boolean)
    suspend fun storeLocalTemperature(temperature: Int)
    suspend fun storeLocalFanSpeed(fanSpeed: Int)
    suspend fun storeLocalIsFrontDefrosterOn(isOn: Boolean)
}
