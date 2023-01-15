package com.imcys.bilibilias.common.data.repository

import com.imcys.bilibilias.common.data.dao.DownloadFinishTaskDao
import com.imcys.bilibilias.common.data.entity.DownloadFinishTaskInfo


class DownloadFinishTaskRepository(private val dao: DownloadFinishTaskDao) {


    val allDownloadFinishTask = dao.getByIdOrderList()

    suspend fun insert(todo: DownloadFinishTaskInfo) {
        dao.insert(todo)
    }


    suspend fun deleteAndReturnList(todo: DownloadFinishTaskInfo): MutableList<DownloadFinishTaskInfo> {
        dao.delete(todo)
        return dao.getByIdOrderList()
    }


}