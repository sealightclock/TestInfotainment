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
     * Updates only the Power state on the vehicle platform.
     */
    suspend fun updatePlatformIsPowerOn(isPowerOn: Boolean)

    /**
     * Updates only the Temperature on the vehicle platform.
     */
    suspend fun updatePlatformTemperature(temperature: Int)

    /**
     * Updates only the Fan Speed on the vehicle platform.
     */
    suspend fun updatePlatformFanSpeed(fanSpeed: Int)

    /**
     * Updates only the Front Defroster state on the vehicle platform.
     */
    suspend fun updatePlatformIsFrontDefrosterOn(isOn: Boolean)

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

    /**
     * Persists the Power state to local storage.
     */
    suspend fun storeLocalIsPowerOn(isPowerOn: Boolean)

    /**
     * Persists the Temperature to local storage.
     */
    suspend fun storeLocalTemperature(temperature: Int)

    /**
     * Persists the Fan Speed to local storage.
     */
    suspend fun storeLocalFanSpeed(fanSpeed: Int)

    /**
     * Persists the Front Defroster state to local storage.
     */
    suspend fun storeLocalIsFrontDefrosterOn(isOn: Boolean)
}
