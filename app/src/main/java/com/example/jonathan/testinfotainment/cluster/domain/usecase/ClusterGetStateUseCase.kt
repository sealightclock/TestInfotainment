package com.example.jonathan.testinfotainment.cluster.domain.usecase

import com.example.jonathan.testinfotainment.cluster.domain.ClusterEntity
import com.example.jonathan.testinfotainment.cluster.domain.ClusterRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for retrieving the current cluster state (system settings & location properties).
 *
 * @property repository The Cluster repository for accessing cluster data.
 */
class ClusterGetStateUseCase(private val repository: ClusterRepository) {
    /**
     * Executes the use case to obtain a flow of [ClusterEntity].
     */
    operator fun invoke(): Flow<ClusterEntity> = repository.getClusterState()
}
