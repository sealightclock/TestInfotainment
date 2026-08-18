package com.example.jonathan.testinfotainment.hvac.data

import com.example.jonathan.testinfotainment.hvac.domain.HvacEntity
import com.example.jonathan.testinfotainment.hvac.domain.HvacRepository
import kotlinx.coroutines.flow.Flow

class HvacRepositoryImpl(private val dataSource: HvacDataSource) : HvacRepository {
    override fun getHvacState(): Flow<HvacEntity> = dataSource.hvacState

    override suspend fun updateHvacState(hvacEntity: HvacEntity) {
        dataSource.updateState(hvacEntity)
    }

    override suspend fun saveSettingsBackup(temperature: Int, fanSpeed: Int) {
        dataSource.saveBackup(temperature, fanSpeed)
    }

    override suspend fun getSettingsBackup(): Pair<Int, Int> {
        return dataSource.getBackup()
    }
}
