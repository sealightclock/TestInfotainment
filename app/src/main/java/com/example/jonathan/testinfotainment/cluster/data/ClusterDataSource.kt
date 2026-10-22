package com.example.jonathan.testinfotainment.cluster.data

import com.example.jonathan.testinfotainment.cluster.domain.ClusterEntity
import kotlinx.coroutines.flow.Flow

/**
 * Interface for Cluster data sources providing system settings and location properties.
 */
interface ClusterDataSource {
    /**
     * Observable flow emitting current cluster data.
     */
    val clusterState: Flow<ClusterEntity>
}
