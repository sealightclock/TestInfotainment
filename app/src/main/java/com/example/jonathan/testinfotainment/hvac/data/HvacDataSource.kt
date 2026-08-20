package com.example.jonathan.testinfotainment.hvac.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.jonathan.testinfotainment.common.Constants
import com.example.jonathan.testinfotainment.hvac.domain.HvacEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "hvac_settings")

class HvacDataSource(private val context: Context) {

    internal object PreferencesKeys {
        val IS_POWER_ON = booleanPreferencesKey("is_power_on")
        val TEMPERATURE = intPreferencesKey("temperature")
        val FAN_SPEED = intPreferencesKey("fan_speed")
        val IS_FRONT_DEFROSTER_ON = booleanPreferencesKey("is_front_defroster_on")
    }

    val hvacState: Flow<HvacEntity> = context.dataStore.data.map { preferences ->
        val isDefrosterOn = preferences[PreferencesKeys.IS_FRONT_DEFROSTER_ON] ?: false
        HvacEntity(
            isPowerOn = preferences[PreferencesKeys.IS_POWER_ON] ?: true,
            temperature = if (isDefrosterOn) Constants.TEMPERATURE_MAX else (preferences[PreferencesKeys.TEMPERATURE] ?: Constants.TEMPERATURE_DEFAULT),
            fanSpeed = if (isDefrosterOn) Constants.FAN_SPEED_MAX else (preferences[PreferencesKeys.FAN_SPEED] ?: Constants.FAN_SPEED_MIN),
            isFrontDefrosterOn = isDefrosterOn
        )
    }

    suspend fun updateState(newState: HvacEntity) {
        context.dataStore.edit { preferences ->
            val wasDefrosting = preferences[PreferencesKeys.IS_FRONT_DEFROSTER_ON] ?: false

            preferences[PreferencesKeys.IS_POWER_ON] = newState.isPowerOn

            // Only save temperature/fan if we are NOT in defroster mode.
            // When defroster is ON, we show HI/MAX but don't overwrite user preferences in DataStore.
            // When defroster is turned OFF, we revert to the previously stored values.
            if (!wasDefrosting && !newState.isFrontDefrosterOn) {
                preferences[PreferencesKeys.TEMPERATURE] = newState.temperature
                preferences[PreferencesKeys.FAN_SPEED] = newState.fanSpeed
            }

            preferences[PreferencesKeys.IS_FRONT_DEFROSTER_ON] = newState.isFrontDefrosterOn
        }
    }

    suspend fun setInt(key: Preferences.Key<Int>, value: Int) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun getInt(key: Preferences.Key<Int>, defaultValue: Int): Int {
        return try {
            context.dataStore.data.first()[key] ?: defaultValue
        } catch (e: IOException) {
            defaultValue
        }
    }

    suspend fun setBoolean(key: Preferences.Key<Boolean>, value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun getBoolean(key: Preferences.Key<Boolean>, defaultValue: Boolean): Boolean {
        return try {
            context.dataStore.data.first()[key] ?: defaultValue
        } catch (e: IOException) {
            defaultValue
        }
    }
}
