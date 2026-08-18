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
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "hvac_settings")

class HvacDataSource(private val context: Context) {

    private object PreferencesKeys {
        val IS_POWER_ON = booleanPreferencesKey("is_power_on")
        val TEMPERATURE = intPreferencesKey("temperature")
        val FAN_SPEED = intPreferencesKey("fan_speed")
        val IS_FRONT_DEFROSTER_ON = booleanPreferencesKey("is_front_defroster_on")
    }

    val hvacState: Flow<HvacEntity> = context.dataStore.data.map { preferences ->
        HvacEntity(
            isPowerOn = preferences[PreferencesKeys.IS_POWER_ON] ?: true,
            temperature = preferences[PreferencesKeys.TEMPERATURE] ?: Constants.TEMPERATURE_DEFAULT,
            fanSpeed = preferences[PreferencesKeys.FAN_SPEED] ?: Constants.FAN_SPEED_MIN,
            isFrontDefrosterOn = preferences[PreferencesKeys.IS_FRONT_DEFROSTER_ON] ?: false
        )
    }

    suspend fun updateState(newState: HvacEntity) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_POWER_ON] = newState.isPowerOn
            preferences[PreferencesKeys.TEMPERATURE] = newState.temperature
            preferences[PreferencesKeys.FAN_SPEED] = newState.fanSpeed
            preferences[PreferencesKeys.IS_FRONT_DEFROSTER_ON] = newState.isFrontDefrosterOn
        }
    }
}
