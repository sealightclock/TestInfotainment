package com.example.jonathan.testinfotainment.cluster.presentation

import java.util.Locale

/**
 * UI state for the Cluster section, holding system settings and location properties.
 *
 * @property brightness Displayed system brightness level percentage (0 - 100%).
 * @property latitude GNSS latitude coordinate.
 * @property longitude GNSS longitude coordinate.
 */
data class ClusterState(
    val brightness: Int = 85,
    val latitude: Double = 37.7749,
    val longitude: Double = -122.4194,
) {
    /** Formatted string representation of the GNSS location coordinates. */
    val formattedCoordinates: String
        get() = String.format(Locale.US, "%.4f° N, %.4f° W", latitude, -longitude)
}
