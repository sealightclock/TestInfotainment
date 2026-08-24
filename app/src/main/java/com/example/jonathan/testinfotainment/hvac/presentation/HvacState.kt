package com.example.jonathan.testinfotainment.hvac.presentation

import com.example.jonathan.testinfotainment.common.Constants
import com.example.jonathan.testinfotainment.hvac.domain.HvacEntity

data class HvacState(
    val hvac: HvacEntity = HvacEntity(),
    val isPowerButtonEnabled: Boolean = true,
    val isFrontDefrosterButtonEnabled: Boolean = true
) {
    val isPowerOn get() = hvac.isPowerOn
    val isFrontDefrosterOn get() = hvac.isFrontDefrosterOn

    val temperature: Int
        get() = if (hvac.isFrontDefrosterOn) Constants.TEMPERATURE_MAX else hvac.temperature

    val fanSpeed: Int
        get() = if (hvac.isFrontDefrosterOn) Constants.FAN_SPEED_MAX else hvac.fanSpeed
}
