package com.imcys.bilibilias.database.entity.download

enum class DownloadStage {
    /** 下载阶段 */
    DOWNLOAD,

    /** 合并阶段 */
    MERGE
}


enum class DownloadState {
    /** 等待下载 */
    WAITING,

    /** 暂停 */
    PAUSE,

    /** 下载中 */
    DOWNLOADING,

    /** 下载完成 */
    COMPLETED,

    /** 下载异常 */
    ERROR,
}

enum class DownloadSubTaskType {
    VIDEO,
    AUDIO
}
