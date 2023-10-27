package com.imcys.bilibilias.common.data.repository

import com.imcys.bilibilias.common.data.dao.DownloadTaskDao
import com.imcys.bilibilias.common.data.entity.DownloadTaskInfo
import com.imcys.bilibilias.common.di.AppCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadTaskRepository @Inject constructor(private val dao: DownloadTaskDao,@AppCoroutineScope private val scope: CoroutineScope) {

    suspend fun allDownloadTask() = dao.getByIdOrderList()

    fun insert(info: DownloadTaskInfo) {
        scope.launch {
            dao.insert(info)
        }
    }

    fun deleteAndReturnList(info: DownloadTaskInfo): List<DownloadTaskInfo> {
        scope.launch {
            return@launch withContext(scope.coroutineContext) {
                dao.delete(info)
                dao.getByIdOrderList()
            }
        }
        return emptyList()
    }

    fun delete(info: DownloadTaskInfo) {
        scope.launch {
            dao.delete(info)
        }
    }

    suspend fun findById(taskId: Int) = dao.findById(taskId)
    fun update(info: DownloadTaskInfo) {
        scope.launch {
            dao.update(info)
        }
    }

    suspend fun findByCidAndTag(cid: Long, tag: String) =
        dao.findByCidAndTag(cid, tag)
}
