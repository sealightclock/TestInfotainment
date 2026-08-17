package com.example.jonathan.testinfotainment.hvac.domain

data class HvacEntity(
    val isPowerOn: Boolean = true,
    val temperature: Int = 75,
    val fanSpeed: Int = 1,
    val isFrontDefrosterOn: Boolean = false
)
