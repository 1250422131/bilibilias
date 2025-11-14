package com.imcys.bilibilias.database.entity.download

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.imcys.bilibilias.database.converter.DateConverter
import com.imcys.bilibilias.database.converter.download.DownloadModeConverter
import com.imcys.bilibilias.database.converter.download.DownloadPlatformConverter
import com.imcys.bilibilias.database.converter.download.DownloadStageConverter
import com.imcys.bilibilias.database.converter.download.DownloadStateConverter
import com.imcys.bilibilias.database.converter.download.DownloadTaskNodeTypeConverter
import com.imcys.bilibilias.database.converter.download.DownloadTaskTypeConverter
import com.imcys.bilibilias.database.converter.download.NamingConventionConverter
import java.util.Date

/**
 * 下载任务（顶层）
 */
@Entity(tableName = "download_task")
@TypeConverters(
    DownloadPlatformConverter::class,
    DateConverter::class,
    DownloadTaskTypeConverter::class
)
data class DownloadTask(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    val taskId: Long = 0,

    @ColumnInfo(name = "platform_id")
    val platformId: String,          // bvid 或其余平台 id

    @ColumnInfo(name = "download_platform")
    val downloadPlatform: DownloadPlatform,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String = "",

    @ColumnInfo(name = "cover")
    val cover: String,

    @ColumnInfo(name = "type")
    val type: DownloadTaskType,


    @ColumnInfo(name = "created_time")
    val createTime: Date = Date(),

    @ColumnInfo(name = "update_time")
    val updateTime: Date = Date()
)

/**
 * 下载任务节点
 */
@Entity(
    tableName = "download_task_node",
    foreignKeys = [ForeignKey(
        entity = DownloadTask::class,
        parentColumns = ["task_id"],
        childColumns = ["task_id"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = DownloadTaskNode::class,
        parentColumns = ["node_id"],
        childColumns = ["parent_node_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("task_id"), Index("parent_node_id")]
)
@TypeConverters(DownloadTaskNodeTypeConverter::class, DateConverter::class)
data class DownloadTaskNode(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "node_id")
    val nodeId: Long = 0,

    @ColumnInfo(name = "task_id")
    val taskId: Long,                // 归属任务

    @ColumnInfo(name = "parent_node_id")
    val parentNodeId: Long? = null,         // 父节点

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "pic")
    val pic: String? = null,


    @ColumnInfo(name = "platform_id")
    val platformId: String,          // 平台节点唯一 id

    @ColumnInfo(name = "node_type")
    val nodeType: DownloadTaskNodeType,

    @ColumnInfo(name = "created_time")
    val createTime: Date = Date(),

    @ColumnInfo(name = "update_time")
    val updateTime: Date = Date()
)

/**
 * 最终下载片段
 */
@Entity(
    tableName = "download_segment",
    foreignKeys = [ForeignKey(
        entity = DownloadTaskNode::class,
        parentColumns = ["node_id"],
        childColumns = ["node_id"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = DownloadTask::class,
        parentColumns = ["task_id"],
        childColumns = ["task_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("node_id"), Index("task_id")]
)
@TypeConverters(
    DownloadModeConverter::class, DateConverter::class,
    DownloadStateConverter::class, DownloadStageConverter::class,
    NamingConventionConverter::class
)
data class DownloadSegment(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "segment_id")
    val segmentId: Long = 0,

    @ColumnInfo(name = "node_id")
    val nodeId: Long,

    @ColumnInfo(name = "task_id")
    val taskId: Long? = null, // 关联任务

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "cover")
    val cover: String?,

    @ColumnInfo(name = "segment_order")
    val segmentOrder: Long,

    @ColumnInfo(name = "platform_id")
    val platformId: String,

    // 平台唯一ID，如果是B站视频/番剧那就是CID，不同的平台唯一ID不同
    @ColumnInfo(name = "platform_unique_id", defaultValue = "")
    val platformUniqueId: String,

    @ColumnInfo(name = "platform_info")
    val platformInfo: String, // 平台JSON

    @ColumnInfo(name = "download_mode")
    val downloadMode: DownloadMode,

    @ColumnInfo(name = "save_path")
    val savePath: String,

    @ColumnInfo(name = "file_size")
    val fileSize: Long,

    @ColumnInfo(name = "duration")
    val duration: Long?,

    @ColumnInfo(name = "download_state")
    val downloadState: DownloadState = DownloadState.WAITING,

    @ColumnInfo(name = "naming_convention_info")
    val namingConventionInfo: NamingConventionInfo? = null,

    @ColumnInfo(name = "created_time")
    val createTime: Date = Date(),

    @ColumnInfo(name = "update_time")
    val updateTime: Date = Date(),
) {
    // 仅仅用于临时存储视频时长等信息，不存数据库
    @Ignore
    var tempDuration: Long = 0
}