package com.example.jonathan.testinfotainment.hvac.domain

/**
 * Enum representing the HVAC properties that can be persisted in local storage.
 */
enum class HvacDataStoreDto {
    /** The set temperature value. */
    TEMPERATURE,
    /** The set fan speed level. */
    FAN_SPEED,
    /** Whether the system is powered on. */
    IS_POWER_ON,
    /** Whether the front defroster is active. */
    IS_FRONT_DEFROSTER_ON
}
