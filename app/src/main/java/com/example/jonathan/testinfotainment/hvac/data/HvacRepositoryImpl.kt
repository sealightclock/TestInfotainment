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
    private val platformDataSource: HvacPlatformDataSource
) : HvacRepository {
    
    // UI observes the platform state as the single source of truth for the vehicle
    override fun getHvacState(): Flow<HvacEntity> = platformDataSource.hvacState

    override suspend fun updateHvacState(hvacEntity: HvacEntity) {
        // Send updates to the platform (VHAL)
        platformDataSource.updateState(hvacEntity)
        // Persist to local storage if needed (e.g. for resume on next boot)
        localDataSource.updateState(hvacEntity)
    }
}
