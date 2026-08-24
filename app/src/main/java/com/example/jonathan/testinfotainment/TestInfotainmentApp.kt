package com.example.jonathan.testinfotainment

import android.app.Application

/**
 * Custom [Application] class for the TestInfotainment app.
 *
 * This class is registered in the AndroidManifest.xml and is instantiated before any other
 * component (Activity, Service, etc.) when the application process starts.
 *
 * Common uses for this class include:
 * 1. Global Initialization: Setting up libraries like Dependency Injection (e.g., Hilt, Koin),
 *    logging (e.g., Timber), or crash reporting.
 * 2. Providing Global Context: Serving as a base context that persists throughout the life
 *    of the application process.
 * 3. App-wide State Management: Holding non-UI related state that needs to survive across
 *    different Activity instances.
 */
class TestInfotainmentApp : Application()

