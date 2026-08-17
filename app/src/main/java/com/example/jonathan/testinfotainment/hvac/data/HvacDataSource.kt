package com.example.jonathan.testinfotainment.hvac.data

import com.example.jonathan.testinfotainment.hvac.domain.HvacEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HvacDataSource {
    private val _hvacState = MutableStateFlow(HvacEntity())
    val hvacState: StateFlow<HvacEntity> = _hvacState

    fun updateState(newState: HvacEntity) {
        _hvacState.value = newState
    }
}
