package com.example.jonathan.testinfotainment

import android.app.Application
import com.example.jonathan.testinfotainment.common.AppContainer
import com.example.jonathan.testinfotainment.common.AppContainerImpl

/**
 * Custom [Application] class for the TestInfotainment app.
 * Initializes and holds the [AppContainer] which serves as the dependency injection hub.
 */
class TestInfotainmentApp : Application() {
    /**
     * Instance of AppContainer used by all components of the app for dependency injection.
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        // Initialize the dependency container.
        container = AppContainerImpl(this)
    }
}

