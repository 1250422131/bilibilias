package com.bilias.feature.download

import com.imcys.model.download.Entry
import java.io.File

data class Task(
    val avid: Long = 0,
    val bvid: String = "",
    val cid: Long = 0,
    val cover: String = "",
    val ownerName: String = "",
    val title: String = "",
    val height: Int = 0,
    val width: Int = 0,
    val vFile: File? = null,
    val aFile: File? = null,
    val dFile: File? = null,
)

fun Entry.mapToTask(): Task = Task(
    avid = avid,
    bvid = bvid,
    cid = pageData.cid,
    cover = cover,
    ownerName = ownerName,
    title = title,
    height = pageData.height,
    width = pageData.width,
    vFile = vFile,
    aFile = aFile,
    dFile = dFile
)
