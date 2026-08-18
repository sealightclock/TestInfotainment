package com.example.jonathan.testinfotainment.hvac.domain

import kotlinx.coroutines.flow.Flow

interface HvacRepository {
    fun getHvacState(): Flow<HvacEntity>
    suspend fun updateHvacState(hvacEntity: HvacEntity)
    suspend fun saveSettingsBackup(temperature: Int, fanSpeed: Int)
    suspend fun getSettingsBackup(): Pair<Int, Int>
}
