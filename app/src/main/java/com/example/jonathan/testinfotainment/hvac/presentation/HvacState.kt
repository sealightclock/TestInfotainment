package com.example.jonathan.testinfotainment.hvac.presentation

import com.example.jonathan.testinfotainment.common.Constants

data class HvacState(
    val isPowerOn: Boolean = true,
    val temperature: Int = Constants.TEMPERATURE_DEFAULT,
    val fanSpeed: Int = Constants.FAN_SPEED_MIN,
    val isFrontDefrosterOn: Boolean = false
)
