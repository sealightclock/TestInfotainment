package com.example.jonathan.testinfotainment.hvac.data

import com.example.jonathan.testinfotainment.hvac.domain.HvacEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Placeholder for Platform/VHAL data source.
 * This will handle real-time communication with the vehicle's hardware.
 */
class HvacPlatformDataSource : HvacDataSource {
    private val _hvacState = MutableStateFlow(HvacEntity())
    override val hvacState: Flow<HvacEntity> = _hvacState

    override suspend fun updateState(newState: HvacEntity) {
        // In a real implementation, this would call CarPropertyManager or similar APIs
        _hvacState.value = newState
    }
}
