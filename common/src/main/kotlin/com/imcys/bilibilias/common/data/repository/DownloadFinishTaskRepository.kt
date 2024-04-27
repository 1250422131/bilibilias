package com.imcys.bilibilias.common.data.repository

import com.imcys.bilibilias.common.data.dao.DownloadFinishTaskDao
import com.imcys.bilibilias.common.data.entity.DownloadFinishTaskInfo

class DownloadFinishTaskRepository(private val dao: DownloadFinishTaskDao) {

    suspend fun allDownloadFinishTask() = dao.getByIdOrderList()

    suspend fun insert(todo: DownloadFinishTaskInfo) {
        dao.insert(todo)
    }

    suspend fun deleteAndReturnList(todo: DownloadFinishTaskInfo): MutableList<DownloadFinishTaskInfo> {
        dao.delete(todo)
        return dao.getByIdOrderList()
    }

    suspend fun delete(todo: DownloadFinishTaskInfo) {
        dao.delete(todo)
    }

    suspend fun findById(taskId: Int) = dao.findById(taskId)
}
