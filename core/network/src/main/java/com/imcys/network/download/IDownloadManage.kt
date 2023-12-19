package com.imcys.network.download

import com.imcys.model.download.Entry

interface IDownloadManage {
    /** 获取给定路径下的所有任务 */
    fun getAllTask(path: String): List<Entry>

    fun downloadDanmaku(cid: Long, second: Int)
}
