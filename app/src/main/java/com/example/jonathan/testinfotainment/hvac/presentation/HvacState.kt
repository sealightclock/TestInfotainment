package com.example.jonathan.testinfotainment.hvac.presentation

data class HvacState(
    val isPowerOn: Boolean = true,
    val temperature: Int = 75,
    val fanSpeed: Int = 1,
    val isFrontDefrosterOn: Boolean = false
)
