package com.example.jonathan.testinfotainment.hvac.data

import com.example.jonathan.testinfotainment.hvac.domain.HvacEntity
import com.example.jonathan.testinfotainment.hvac.domain.HvacProperty
import com.example.jonathan.testinfotainment.hvac.domain.HvacRepository
import kotlinx.coroutines.flow.Flow

class HvacRepositoryImpl(private val dataSource: HvacDataSource) : HvacRepository {
    override fun getHvacState(): Flow<HvacEntity> = dataSource.hvacState

    override suspend fun updateHvacState(hvacEntity: HvacEntity) {
        dataSource.updateState(hvacEntity)
    }

    override suspend fun saveInt(property: HvacProperty, value: Int) {
        dataSource.setInt(property.toIntKey(), value)
    }

    override suspend fun getInt(property: HvacProperty, defaultValue: Int): Int {
        return dataSource.getInt(property.toIntKey(), defaultValue)
    }

    override suspend fun saveBoolean(property: HvacProperty, value: Boolean) {
        dataSource.setBoolean(property.toBooleanKey(), value)
    }

    override suspend fun getBoolean(property: HvacProperty, defaultValue: Boolean): Boolean {
        return dataSource.getBoolean(property.toBooleanKey(), defaultValue)
    }

    private fun HvacProperty.toIntKey() = when (this) {
        HvacProperty.TEMPERATURE -> HvacDataSource.PreferencesKeys.TEMPERATURE
        HvacProperty.FAN_SPEED -> HvacDataSource.PreferencesKeys.FAN_SPEED
        else -> throw IllegalArgumentException("Property $this is not an Int type")
    }

    private fun HvacProperty.toBooleanKey() = when (this) {
        HvacProperty.IS_POWER_ON -> HvacDataSource.PreferencesKeys.IS_POWER_ON
        HvacProperty.IS_FRONT_DEFROSTER_ON -> HvacDataSource.PreferencesKeys.IS_FRONT_DEFROSTER_ON
        else -> throw IllegalArgumentException("Property $this is not a Boolean type")
    }
}
