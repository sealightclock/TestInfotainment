package com.example.jonathan.testinfotainment.hvac.data

import com.example.jonathan.testinfotainment.hvac.domain.HvacEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * Simulates Platform/VHAL data source.
 * Handles updates immediately but simulates a delayed feedback loop.
 */
class HvacPlatformDataSource : HvacDataSource {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _hvacState = MutableStateFlow(HvacEntity())
    override val hvacState: Flow<HvacEntity> = _hvacState

    override suspend fun updateState(newState: HvacEntity) {
        // [1] Received immediately. 
        // [2] Simulated platform logic: waits for 1 second, then sends the value back.
        // We launch in a separate scope so we don't block the caller (Repository/UseCase).
        scope.launch {
            delay(1000)
            _hvacState.value = newState
        }
    }
}
