package com.imcys.bilibilias.database.entity.download

enum class DownloadStage {
    /** 下载阶段 */
    DOWNLOAD,

    /** 合并阶段 */
    MERGE,

    /** 文件移动阶段 */
    MOVE,
}


enum class DownloadState {
    /** 等待下载 */
    WAITING,

    /** 暂停 */
    PAUSE,

    /** 下载中 */
    DOWNLOADING,

    /** 合并中 */
    MERGING,

    /** 下载完成 */
    COMPLETED,

    /** 下载异常 */
    ERROR,

    /** 已取消 */
    CANCELLED,

    /** 前置任务 */
    PRE_TASK,

    /** 后置任务 */
    POST_TASK,
}

enum class DownloadSubTaskType {
    VIDEO,
    AUDIO
}
