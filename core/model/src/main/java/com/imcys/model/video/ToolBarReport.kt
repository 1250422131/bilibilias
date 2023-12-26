package com.imcys.model.video

/**
 * @param aid 稿件avid
 * @param view 播放数
 * @param danmaku 弹幕数
 * @param reply 评论数
 * @param favorite 收藏数
 * @param coin 投币数
 * @param share 分享数
 * @param like 获赞数
 * @param evaluation 视频评分
 * @param isLike 是否点赞
 * @param isCoin 是否投币
 * @param isFavoured 是否收藏
 */
data class ToolBarReport(
    val like: Int = 0,
    val coin: Int = 0,
    val favorite: Int = 0,
    val danmaku: Int = 0,
    val evaluation: String = "",
    val reply: Int = 0,
    val share: Int = 0,
    val view: Int = 0,
    val isLike: Boolean = false,
    val isCoin: Boolean = false,
    val isFavoured: Boolean = false
)
