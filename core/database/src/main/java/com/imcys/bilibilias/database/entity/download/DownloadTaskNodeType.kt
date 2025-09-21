package com.imcys.bilibilias.database.entity.download

/**
 * 下载视频中间节点信息
 */
enum class DownloadTaskNodeType {
    BILI_VIDEO_PAGE,// 分P
    BILI_VIDEO_SECTION_EPISODES, // 合集章节

    BILI_DONGHUA_SEASON, // 番剧季度

    BILI_DONGHUA_SECTION, // 番剧预告

    BILI_DONGHUA_EPISOD, // 番剧正片 -> 首次于纪录片发现

    BILI_VIDEO_INTERACTIVE, // 互动视频

    ACFUN_VIDEO_PAGE // AcFun暂无实现
}


enum class DownloadTaskType {
    BILI_VIDEO,
    BILI_VIDEO_SECTION,
    BILI_DONGHUA,
}