package com.example.jonathan.testinfotainment.common

/**
 * Global constants used throughout the application.
 */
object Constants {
    /**
     * Delays (Timer Intents or Events)
     */

    // Some buttons, such as Power and Front Defroster, needs to be disabled during a cool-down
    // period:
    const val DELAY_VIEW_DISABLED_TO_ENABLED = 2000L

    // Platform values, once reaching the Presentation layer, should be ignored if too close to
    // last user input values::
    const val DELAY_DATA_CONCURRENCY_UI_TO_PLATFORM = 2000L

    // This is to simulate the round-trip time of data from the Platform to the Backend then back
    // to the Platform:
    const val DELAY_DATA_BACKEND_TO_PLATFORM = 1000L

    // Additional delay for Platform values to reach the Intent queue.
    // Ideally: DELAY_DATA_CONCURRENCY_UI_TO_PLATFORM =
    //    DELAY_DATA_BACKEND_TO_PLATFORM +
    //    DELAY_DATA_PLATFORM_TO_VIEWMODEL_TO_INTENT
    const val DELAY_DATA_PLATFORM_TO_VIEWMODEL_TO_INTENT =
        DELAY_DATA_CONCURRENCY_UI_TO_PLATFORM - DELAY_DATA_BACKEND_TO_PLATFORM
}
