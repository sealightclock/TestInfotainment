package com.example.jonathan.testinfotainment.cluster.data

import com.example.jonathan.testinfotainment.cluster.domain.ClusterEntity
import com.example.jonathan.testinfotainment.cluster.domain.ClusterRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of [ClusterRepository] coordinating data from platform and local data sources.
 *
 * @property platformDataSource Data source for real-time device system settings and location.
 * @property localDataSource Local fallback data source.
 */
class ClusterRepositoryImpl(
    private val platformDataSource: ClusterPlatformDataSource,
    private val localDataSource: ClusterLocalDataSource,
) : ClusterRepository {

    override fun getClusterState(): Flow<ClusterEntity> = platformDataSource.clusterState

    override fun getLocalClusterState(): Flow<ClusterEntity> = localDataSource.clusterState
}
