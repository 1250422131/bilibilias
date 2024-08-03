package com.imcys.model.download

import java.io.File

data class CacheFile(
    // 如果是合集则为0，如果是章节则是1
    val flag: Int,
    // 合集名
    val collectionName: String,
    // 章节名
    val chapterName: String,
    // audio路径
    val audio: File,
    // video路径
    val video: File,
    // 弹幕文件路径
    val danmaku: File,
    // 图片封面地址
    val cover: String,
    val chapters: List<Chapter>,
    val bvId: String,
    val avId: Long
) {
    data class Chapter(val cid: Int, val title: String)
}
