package com.example.jonathan.testinfotainment.hvac.data

import com.example.jonathan.testinfotainment.hvac.domain.HvacEntity
import com.example.jonathan.testinfotainment.hvac.domain.HvacDataStoreDto
import com.example.jonathan.testinfotainment.hvac.domain.HvacRepository
import kotlinx.coroutines.flow.Flow

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

    override suspend fun saveInt(property: HvacDataStoreDto, value: Int) {
        localDataSource.setInt(property.toIntKey(), value)
    }

    override suspend fun getInt(property: HvacDataStoreDto, defaultValue: Int): Int {
        return localDataSource.getInt(property.toIntKey(), defaultValue)
    }

    override suspend fun saveBoolean(property: HvacDataStoreDto, value: Boolean) {
        localDataSource.setBoolean(property.toBooleanKey(), value)
    }

    override suspend fun getBoolean(property: HvacDataStoreDto, defaultValue: Boolean): Boolean {
        return localDataSource.getBoolean(property.toBooleanKey(), defaultValue)
    }

    private fun HvacDataStoreDto.toIntKey() = when (this) {
        HvacDataStoreDto.TEMPERATURE -> HvacLocalDataSource.PreferencesKeys.TEMPERATURE
        HvacDataStoreDto.FAN_SPEED -> HvacLocalDataSource.PreferencesKeys.FAN_SPEED
        else -> throw IllegalArgumentException("Property $this is not an Int type")
    }

    private fun HvacDataStoreDto.toBooleanKey() = when (this) {
        HvacDataStoreDto.IS_POWER_ON -> HvacLocalDataSource.PreferencesKeys.IS_POWER_ON
        HvacDataStoreDto.IS_FRONT_DEFROSTER_ON -> HvacLocalDataSource.PreferencesKeys.IS_FRONT_DEFROSTER_ON
        else -> throw IllegalArgumentException("Property $this is not a Boolean type")
    }
}
