package com.example.jonathan.testinfotainment.cluster.domain

/**
 * Domain entity representing safety-critical cluster and vehicle telemetry data.
 *
 * @property brightness System screen brightness percentage (0 - 100).
 * @property latitude GNSS latitude coordinate in degrees.
 * @property longitude GNSS longitude coordinate in degrees.
 */
data class ClusterEntity(
    val brightness: Int = 85,
    val latitude: Double = 37.7749,
    val longitude: Double = -122.4194,
)
