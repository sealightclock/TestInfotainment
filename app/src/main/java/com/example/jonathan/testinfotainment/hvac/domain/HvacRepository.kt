package com.example.jonathan.testinfotainment.hvac.domain

import kotlinx.coroutines.flow.Flow

interface HvacRepository {
    fun getHvacState(): Flow<HvacEntity>
    suspend fun updateHvacState(hvacEntity: HvacEntity)
    
    suspend fun saveInt(property: HvacProperty, value: Int)
    suspend fun getInt(property: HvacProperty, defaultValue: Int): Int
    suspend fun saveBoolean(property: HvacProperty, value: Boolean)
    suspend fun getBoolean(property: HvacProperty, defaultValue: Boolean): Boolean
}
