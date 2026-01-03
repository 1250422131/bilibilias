package com.imcys.bilibilias.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.imcys.bilibilias.database.entity.download.DownloadSegment
import com.imcys.bilibilias.database.entity.download.DownloadTask
import com.imcys.bilibilias.database.entity.download.DownloadTaskNode
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface DownloadTaskDao {


    /**
     *  根据 platformId 查单个任务
     */
    @Query("SELECT * FROM download_task WHERE platform_id = :platformId")
    suspend fun getTaskByPlatformId(platformId: String): DownloadTask?


    @Query("SELECT * FROM download_task WHERE task_id = :taskId")
    suspend fun getTaskById( taskId: Long): DownloadTask?

    @Query("""
        SELECT * 
        FROM download_task 
        WHERE task_id = (
            SELECT task_id 
            FROM download_task_node 
            WHERE node_id = :nodeId
        )
    """)
    suspend fun getTaskByNodeId(nodeId: Long): DownloadTask?

    /**
     *  根据 platformId 查单个节点
     */
    @Query("SELECT * FROM download_task_node WHERE task_id = :taskId AND platform_id = :platformId")
    suspend fun getTaskNodeByTaskIdAndPlatformId(
        taskId: Long,
        platformId: String
    ): DownloadTaskNode?

    @Query("SELECT * FROM download_task_node WHERE node_id = :nodeId")
    suspend fun getTaskNodeByNodeId(
        nodeId: Long,
    ): DownloadTaskNode?
    /**
     *  根据 platformId 查询单个任务
     */
    @Query("SELECT * FROM download_segment WHERE node_id = :nodeId AND platform_id = :platformId")
    suspend fun getSegmentByNodeIdAndPlatformId(nodeId: Long, platformId: String): DownloadSegment?


    @Query("SELECT * FROM download_segment WHERE segment_id = :segmentId")
    suspend fun getSegmentBySegmentId(segmentId: Long): DownloadSegment?


    @Query("SELECT * FROM download_segment ORDER BY segment_id DESC")
    fun getSegmentAll(): Flow<List<DownloadSegment>>


    /**
     * 插入顶层任务
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: DownloadTask): Long

    /**
     * 插入节点
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNode(node: DownloadTaskNode): Long

    /**
     * 插入下载片段
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSegment(segment: DownloadSegment): Long


    /**
     * 更新任务并刷新 updateTime
     */
    @Update
    suspend fun updateTaskRaw(task: DownloadTask)

    suspend fun updateTask(task: DownloadTask) {
        updateTaskRaw(task.copy(updateTime = Date()))
    }

    /**
     * 更新节点并刷新 updateTime
     */
    @Update
    suspend fun updateNodeRaw(node: DownloadTaskNode)

    suspend fun updateNode(node: DownloadTaskNode) {
        updateNodeRaw(node.copy(updateTime = Date()))
    }

    /**
     * 更新片段并刷新 updateTime
     */
    @Update
    suspend fun updateSegmentRaw(segment: DownloadSegment)

    suspend fun updateSegment(segment: DownloadSegment) {
        updateSegmentRaw(segment.copy(updateTime = Date()))
    }


    /**
     * 按 taskId 删除整棵树（级联删除节点和片段）
     */
    @Query("DELETE FROM download_task WHERE task_id = :taskId")
    suspend fun deleteTask(taskId: Long)

    /**
     * 按 nodeId 删除节点及其所有子节点和片段
     */
    @Query("DELETE FROM download_task_node WHERE node_id = :nodeId")
    suspend fun deleteNode(nodeId: Long)

    /**
     * 按 segmentId 删除单个片段
     */
    @Query("DELETE FROM download_segment WHERE segment_id = :segmentId")
    suspend fun deleteSegment(segmentId: Long)

}