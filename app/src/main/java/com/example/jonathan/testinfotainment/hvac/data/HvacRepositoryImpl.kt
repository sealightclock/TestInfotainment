package com.example.jonathan.testinfotainment.hvac.data

import com.example.jonathan.testinfotainment.hvac.domain.HvacEntity
import com.example.jonathan.testinfotainment.hvac.domain.HvacRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of [HvacRepository] that coordinates data between
 * a local persistent store and the vehicle platform.
 */
class HvacRepositoryImpl(
    private val localDataSource: HvacLocalDataSource,
    private val platformDataSource: HvacPlatformDataSource,
) : HvacRepository {
    
    // UI observes the platform state as the single source of truth for the vehicle
    override fun getHvacState(): Flow<HvacEntity> = platformDataSource.hvacState

    override suspend fun updatePlatformHvacState(hvacEntity: HvacEntity) {
        // Send updates to the platform (VHAL)
        platformDataSource.updateState(hvacEntity)
    }

    override fun getLocalHvacState(): Flow<HvacEntity> = localDataSource.hvacState

    override suspend fun updateLocalHvacState(hvacEntity: HvacEntity) {
        localDataSource.updateState(hvacEntity)
    }

    override suspend fun storeLocalIsPowerOn(isPowerOn: Boolean) {
        localDataSource.updateIsPowerOn(isPowerOn)
    }

    override suspend fun storeLocalTemperature(temperature: Int) {
        localDataSource.updateTemperature(temperature)
    }

    override suspend fun storeLocalFanSpeed(fanSpeed: Int) {
        localDataSource.updateFanSpeed(fanSpeed)
    }

    override suspend fun storeLocalIsFrontDefrosterOn(isOn: Boolean) {
        localDataSource.updateIsFrontDefrosterOn(isOn)
    }
}
