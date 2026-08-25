package com.example.jonathan.testinfotainment.hvac.data

import android.content.Context
import android.util.Log
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

private const val TAG = "TIF: HvacLocalDataSource"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "hvac_settings")

/**
 * Local data source using DataStore for persisting HVAC settings.
 * This allows the app to remember user preferences across app restarts.
 */
class HvacLocalDataSource(private val context: Context) : HvacDataSource {

    /**
     * Keys used for DataStore preferences.
     */
    internal object PreferencesKeys {
        val IS_POWER_ON = booleanPreferencesKey("is_power_on")
        val TEMPERATURE = intPreferencesKey("temperature")
        val FAN_SPEED = intPreferencesKey("fan_speed")
        val IS_FRONT_DEFROSTER_ON = booleanPreferencesKey("is_front_defroster_on")
    }

    /**
     * Flow that emits the stored HVAC state.
     * Special handling: If front defroster was previously on, it ensures temperature and fan speed
     * are returned as maxed out according to the business rules.
     */
    override val hvacState: Flow<HvacEntity> = context.dataStore.data.map { preferences ->
        val isDefrosterOn = preferences[PreferencesKeys.IS_FRONT_DEFROSTER_ON] ?: false
        HvacEntity(
            isPowerOn = preferences[PreferencesKeys.IS_POWER_ON] ?: true,
            temperature = if (isDefrosterOn) Constants.TEMPERATURE_MAX else (preferences[PreferencesKeys.TEMPERATURE] ?: Constants.TEMPERATURE_DEFAULT),
            fanSpeed = if (isDefrosterOn) Constants.FAN_SPEED_MAX else (preferences[PreferencesKeys.FAN_SPEED] ?: Constants.FAN_SPEED_MIN),
            isFrontDefrosterOn = isDefrosterOn
        )
    }

    /**
     * Updates the persistent storage with a new HVAC state.
     * Special handling: Avoids overwriting temperature and fan speed if the change was 
     * triggered solely by the defroster, preserving the user's previous manual settings.
     *
     * @param newState The new state to persist.
     */
    override suspend fun updateState(newState: HvacEntity) {
        context.dataStore.edit { preferences ->
            val wasDefrosting = preferences[PreferencesKeys.IS_FRONT_DEFROSTER_ON] ?: false

            preferences[PreferencesKeys.IS_POWER_ON] = newState.isPowerOn

            // Only update stored temp/fan if we are NOT in a defrosting state,
            // or if we are just starting/stopping defrosting (to avoid saving the 'HI' override as the base temp).
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
            Log.e(TAG, "getInt: IOException: use defaultValue=[$defaultValue] !!\n${e.stackTraceToString()}")
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
            Log.e(TAG, "getBoolean: IOException: use defaultValue=[$defaultValue] !!\n${e.stackTraceToString()}")
            defaultValue
        }
    }
}
