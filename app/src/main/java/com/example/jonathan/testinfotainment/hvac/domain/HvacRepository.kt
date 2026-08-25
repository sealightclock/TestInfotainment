package com.example.jonathan.testinfotainment.hvac.domain

import kotlinx.coroutines.flow.Flow

interface HvacRepository {
    fun getHvacState(): Flow<HvacEntity>

    suspend fun updateHvacState(hvacEntity: HvacEntity)
    
    suspend fun saveInt(property: HvacDataStoreDto, value: Int)
    suspend fun getInt(property: HvacDataStoreDto, defaultValue: Int): Int

    suspend fun saveBoolean(property: HvacDataStoreDto, value: Boolean)
    suspend fun getBoolean(property: HvacDataStoreDto, defaultValue: Boolean): Boolean
}
