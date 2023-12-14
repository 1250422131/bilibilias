package com.imcys.network.download

import com.imcys.model.download.Entry
import java.io.File

interface IDownloadManage {
    /** 获取给定路径下的所有任务 */
    fun getAllTask(path: String): Map<Entry, MutableList<File>>

    fun downloadDanmaku(cid: Long, second: Int)
}
