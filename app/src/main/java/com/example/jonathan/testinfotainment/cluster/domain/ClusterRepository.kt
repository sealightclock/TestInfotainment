package com.example.jonathan.testinfotainment.cluster.domain

import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for retrieving cluster data (system settings and location properties).
 */
interface ClusterRepository {
    /**
     * Returns a flow of the cluster state from the platform data source.
     */
    fun getClusterState(): Flow<ClusterEntity>

    /**
     * Returns a flow of the local fallback cluster state.
     */
    fun getLocalClusterState(): Flow<ClusterEntity>
}
