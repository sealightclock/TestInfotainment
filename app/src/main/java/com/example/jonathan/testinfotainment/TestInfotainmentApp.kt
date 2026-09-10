package com.example.jonathan.testinfotainment

import android.app.Application
import android.util.Log

private const val TAG = "TIF: App"

/**
 * Custom [Application] class for the TestInfotainment app.
 */
class TestInfotainmentApp : Application() {
    override fun onCreate() {
        Log.i(TAG, "onCreate")

        super.onCreate()
    }
}
