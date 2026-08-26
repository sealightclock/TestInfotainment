package com.example.jonathan.testinfotainment.hvac.data

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

/**
 * Data Transfer Object for HVAC local storage.
 * Contains the keys used for DataStore preferences.
 */
internal object HvacLocalDto {
    val IS_POWER_ON = booleanPreferencesKey("is_power_on")
    val TEMPERATURE = intPreferencesKey("temperature")
    val FAN_SPEED = intPreferencesKey("fan_speed")
    val IS_FRONT_DEFROSTER_ON = booleanPreferencesKey("is_front_defroster_on")
}
