package com.imcys.bilibilias.core.download

import androidx.collection.mutableObjectListOf
import com.imcys.bilibilias.core.common.network.di.ApplicationScope
import com.imcys.bilibilias.core.data.util.ErrorMonitor
import com.imcys.bilibilias.core.data.util.MessageType
import com.imcys.bilibilias.core.database.dao.DownloadTaskDao
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.core.download.chore.DefaultGroupTaskCall
import com.imcys.bilibilias.core.download.task.AsDownloadTask
import com.imcys.bilibilias.core.download.task.GroupTask
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.download.State
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.cause.ResumeFailedCause
import com.liulishuo.okdownload.core.listener.DownloadListener1
import com.liulishuo.okdownload.core.listener.assist.Listener1Assist
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

private const val TAG = "DownloadManager"

class AsDownloadListener @Inject constructor(
    @ApplicationScope private val scope: CoroutineScope,
    private val defaultGroupTaskCall: DefaultGroupTaskCall,
    private val errorMonitor: ErrorMonitor,
    private val taskDao: DownloadTaskDao,
) : DownloadListener1() {
    private val taskQueue = mutableObjectListOf<AsDownloadTask>()

    fun add(task: AsDownloadTask) {
        taskQueue.add(task)
        task.okTask.enqueue(this)
    }

    override fun taskStart(task: DownloadTask, model: Listener1Assist.Listener1Model) {
        scope.launch {
            Napier.d(tag = TAG) { "任务开始 ${task.filename}" }
            val asTask = taskQueue.first { it.okTask === task }
            val info = asTask.ids
            val taskEntity = DownloadTaskEntity(
                uri = asTask.okTask.uri,
                aid = info.aid,
                bvid = info.bvid,
                cid = info.cid,
                fileType = asTask.fileType,
                subTitle = asTask.subTitle,
                title = asTask.title,
                state = State.RUNNING,
            )
            taskDao.insertOrUpdate(taskEntity)
            errorMonitor.addShortErrorMessage("添加任务到下载队列")
        }
    }

    override fun taskEnd(
        task: DownloadTask,
        cause: EndCause,
        realCause: Exception?,
        model: Listener1Assist.Listener1Model,
    ) {
        scope.launch {
            Napier.d(tag = TAG, throwable = realCause) { "任务结束 $cause-${task.filename}" }
            val asTask = taskQueue.first { it.okTask === task }
            val info = asTask.ids
            taskDao.updateStateByUri(
                if (realCause == null) State.COMPLETED else State.ERROR,
                task.uri,
            )

            val tasks = taskDao.findById(info.aid, info.bvid, info.cid)
            val v = tasks.find { it.fileType == FileType.VIDEO }
            val a = tasks.find { it.fileType == FileType.AUDIO }
            if (v != null && v.state == State.COMPLETED) {
                if (a != null && a.state == State.COMPLETED) {
                    defaultGroupTaskCall.execute(GroupTask(v, a))
                }
            }
            errorMonitor.addShortErrorMessage("添加任务到下载队列")
        }
    }

    private fun toast(
        realCause: Exception?,
        task: AsDownloadTask,
    ) {
        val filename = task.okTask.filename
        val messageWithType = if (realCause == null) {
            "$filename·下载成功" to MessageType.Normal
        } else {
            "$filename·下载失败" to MessageType.Error
        }
        errorMonitor.addShortErrorMessage(messageWithType.first, messageWithType.second)
    }

    override fun progress(task: DownloadTask, currentOffset: Long, totalLength: Long) {
        scope.launch {
            Napier.d(tag = TAG) { "下载中: ${task.filename} $currentOffset-$totalLength" }
            taskDao.updateProgressByUri(
                currentOffset,
                totalLength,
                task.uri,
            )
        }
    }

    override fun retry(task: DownloadTask, cause: ResumeFailedCause) {
        Napier.d { task.filename + "重试" + cause }
    }

    override fun connected(
        task: DownloadTask,
        blockCount: Int,
        currentOffset: Long,
        totalLength: Long,
    ) {
        scope.launch {
            Napier.d { "连接结束: ${task.filename}-$currentOffset-$totalLength" }
            taskDao.updateProgressByUri(
                currentOffset,
                totalLength,
                task.uri,
            )
        }
    }
}
