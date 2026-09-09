package com.example.jonathan.testinfotainment.hvac.data

import com.example.jonathan.testinfotainment.common.Constants.DELAY_DATA_BACKEND_TO_PLATFORM
import com.example.jonathan.testinfotainment.hvac.domain.HvacEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Simulates the Vehicle Hardware Abstraction Layer (VHAL) or Platform data source.
 * In a real automotive app, this would interface with the car's hardware properties.
 * Here, it simulates the delayed nature of hardware updates.
 */
class HvacPlatformDataSource : HvacDataSource {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _hvacState = MutableStateFlow(HvacEntity())
    
    /**
     * Observable flow of the platform's current HVAC state.
     */
    override val hvacState: Flow<HvacEntity> = _hvacState

    /**
     * Simulates sending a command to the vehicle hardware.
     * Special handling: Waits for 1 second to simulate hardware processing time before 
     * reflecting the change back in the state flow.
     *
     * @param newState The target HVAC state to apply.
     */
    override suspend fun updateState(newState: HvacEntity) {
        // We launch in a separate scope so we don't block the caller (Repository/UseCase),
        // simulating an asynchronous hardware command.
        scope.launch {
            delay(DELAY_DATA_BACKEND_TO_PLATFORM)
            _hvacState.value = newState
        }
    }

    override suspend fun updateIsPowerOn(isPowerOn: Boolean) {
        scope.launch {
            delay(DELAY_DATA_BACKEND_TO_PLATFORM)
            _hvacState.update { it.copy(isPowerOn = isPowerOn) }
        }
    }

    override suspend fun updateTemperature(temperature: Int) {
        scope.launch {
            delay(DELAY_DATA_BACKEND_TO_PLATFORM)
            _hvacState.update { it.copy(temperature = temperature) }
        }
    }

    override suspend fun updateFanSpeed(fanSpeed: Int) {
        scope.launch {
            delay(DELAY_DATA_BACKEND_TO_PLATFORM)
            _hvacState.update { it.copy(fanSpeed = fanSpeed) }
        }
    }

    override suspend fun updateIsFrontDefrosterOn(isOn: Boolean) {
        scope.launch {
            delay(DELAY_DATA_BACKEND_TO_PLATFORM)
            _hvacState.update { it.copy(isFrontDefrosterOn = isOn) }
        }
    }
}
