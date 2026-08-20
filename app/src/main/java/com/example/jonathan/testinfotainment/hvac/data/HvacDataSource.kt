package com.example.jonathan.testinfotainment.hvac.data

import com.example.jonathan.testinfotainment.hvac.domain.HvacEntity
import kotlinx.coroutines.flow.Flow

/**
 * Common interface for HVAC data sources (Local, Platform/VHAL, etc.)
 */
interface HvacDataSource {
    val hvacState: Flow<HvacEntity>
    suspend fun updateState(newState: HvacEntity)
}
