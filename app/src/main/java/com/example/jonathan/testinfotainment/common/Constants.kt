package com.example.jonathan.testinfotainment.common

/**
 * Global constants used throughout the application for HVAC limits and defaults.
 */
object Constants {
    /** Minimum allowed temperature in Fahrenheit. */
    const val TEMPERATURE_MIN = 63
    /** Default temperature set when the system is first initialized. */
    const val TEMPERATURE_DEFAULT = 75
    /** Maximum allowed temperature in Fahrenheit. Also used as the override for front defrost. */
    const val TEMPERATURE_MAX = 91

    /** Minimum allowed fan speed level. */
    const val FAN_SPEED_MIN = 1
    /** Maximum allowed fan speed level. Also used as the override for front defrost. */
    const val FAN_SPEED_MAX = 7

    /** Delays **/
    const val DELAY_VIEW_DISABLED_TO_ENABLED = 2000L
    const val DELAY_DATA_CONCURRENCY_UI_TO_PLATFORM = 2000L
    const val DELAY_DATA_BACKEND_TO_PLATFORM = 1000L
    const val DELAY_DATA_PLATFORM_TO_VIEWMODEL_TO_INTENT = 1000L
}
