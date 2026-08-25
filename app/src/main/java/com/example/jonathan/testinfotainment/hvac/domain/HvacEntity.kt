package com.example.jonathan.testinfotainment.hvac.domain

import com.example.jonathan.testinfotainment.common.Constants

/**
 * Domain entity representing the state of the vehicle's HVAC system.
 *
 * @property isPowerOn True if the HVAC system is powered on.
 * @property temperature The current temperature setting.
 * @property fanSpeed The current fan speed setting.
 * @property isFrontDefrosterOn True if the front defroster is active.
 */
data class HvacEntity(
    val isPowerOn: Boolean = true,
    val temperature: Int = Constants.TEMPERATURE_DEFAULT,
    val fanSpeed: Int = Constants.FAN_SPEED_MIN,
    val isFrontDefrosterOn: Boolean = false
)
