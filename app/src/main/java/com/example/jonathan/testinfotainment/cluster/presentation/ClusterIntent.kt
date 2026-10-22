package com.example.jonathan.testinfotainment.cluster.presentation

import com.example.jonathan.testinfotainment.cluster.domain.ClusterEntity

/**
 * Intent hierarchy for the Cluster section following MVI architecture.
 * Supports platform refresh events to update system settings and location properties.
 */
sealed class ClusterIntent {
    /**
     * Intent triggered when fresh system settings and location data arrive from the platform data source.
     *
     * @property entity The updated cluster data entity.
     */
    data class PlatformRefreshStateIntent(val entity: ClusterEntity) : ClusterIntent()
}
