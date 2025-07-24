package com.imcys.bilibilias.data.model.download

import com.imcys.bilibilias.database.entity.download.DownloadSegment
import com.imcys.bilibilias.database.entity.download.DownloadTask
import com.imcys.bilibilias.database.entity.download.DownloadTaskNode

/**
 * 供 UI / Domain 层使用的树形结构
 */
data class DownloadTaskTree(
    val task: DownloadTask,
    val roots: List<DownloadTreeNode>        // 顶层节点（parentNodeId == null）
)

data class DownloadTreeNode(
    val node: DownloadTaskNode,
    val segments: List<DownloadSegment>,
    val children: List<DownloadTreeNode>    // 递归子节点
)