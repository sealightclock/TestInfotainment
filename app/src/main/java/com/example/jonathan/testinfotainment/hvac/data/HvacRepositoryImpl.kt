package com.example.jonathan.testinfotainment.hvac.data

import com.example.jonathan.testinfotainment.hvac.domain.HvacEntity
import com.example.jonathan.testinfotainment.hvac.domain.HvacDataStoreDto
import com.example.jonathan.testinfotainment.hvac.domain.HvacRepository
import kotlinx.coroutines.flow.Flow

class HvacRepositoryImpl(private val dataSource: HvacDataSource) : HvacRepository {
    override fun getHvacState(): Flow<HvacEntity> = dataSource.hvacState

    override suspend fun updateHvacState(hvacEntity: HvacEntity) {
        dataSource.updateState(hvacEntity)
    }

    override suspend fun saveInt(property: HvacDataStoreDto, value: Int) {
        dataSource.setInt(property.toIntKey(), value)
    }

    override suspend fun getInt(property: HvacDataStoreDto, defaultValue: Int): Int {
        return dataSource.getInt(property.toIntKey(), defaultValue)
    }

    override suspend fun saveBoolean(property: HvacDataStoreDto, value: Boolean) {
        dataSource.setBoolean(property.toBooleanKey(), value)
    }

    override suspend fun getBoolean(property: HvacDataStoreDto, defaultValue: Boolean): Boolean {
        return dataSource.getBoolean(property.toBooleanKey(), defaultValue)
    }

    private fun HvacDataStoreDto.toIntKey() = when (this) {
        HvacDataStoreDto.TEMPERATURE -> HvacDataSource.PreferencesKeys.TEMPERATURE
        HvacDataStoreDto.FAN_SPEED -> HvacDataSource.PreferencesKeys.FAN_SPEED
        else -> throw IllegalArgumentException("Property $this is not an Int type")
    }

    private fun HvacDataStoreDto.toBooleanKey() = when (this) {
        HvacDataStoreDto.IS_POWER_ON -> HvacDataSource.PreferencesKeys.IS_POWER_ON
        HvacDataStoreDto.IS_FRONT_DEFROSTER_ON -> HvacDataSource.PreferencesKeys.IS_FRONT_DEFROSTER_ON
        else -> throw IllegalArgumentException("Property $this is not a Boolean type")
    }
}
