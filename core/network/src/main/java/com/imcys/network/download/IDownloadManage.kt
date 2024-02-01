package com.imcys.network.download

import com.imcys.model.download.CacheFile

interface IDownloadManage {
    /** 获取给定路径下的所有任务 */
    fun getAllTask(path: String): List<CacheFile>
}
