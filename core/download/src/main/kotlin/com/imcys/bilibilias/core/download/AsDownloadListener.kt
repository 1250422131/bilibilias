package com.imcys.bilibilias.core.download

import androidx.collection.mutableObjectListOf
import com.imcys.bilibilias.core.data.util.ErrorMonitor
import com.imcys.bilibilias.core.database.dao.DownloadTaskDao
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.core.download.chore.DefaultGroupTaskCall
import com.imcys.bilibilias.core.download.task.AsDownloadTask
import com.imcys.bilibilias.core.model.download.State
import com.imcys.bilibilias.core.network.di.ApplicationScope
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.OkDownload
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
    @Suppress("unused") private val okDownload: OkDownload,
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
            val ids = asTask.ids
            val taskEntity = DownloadTaskEntity(
                uri = asTask.okTask.uri,
                aid = ids.aid,
                bvid = ids.bvid,
                cid = ids.cid,
                fileType = asTask.fileType,
                subTitle = asTask.subTitle,
                title = asTask.title,
                state = State.RUNNING,
            )
            taskDao.insertTask(taskEntity)
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
            val state = if (realCause == null) State.COMPLETED else State.ERROR
            asTask.state = state
            val info = asTask.ids
            taskDao.updateStateByUri(state, task.uri)

            val tasks = taskDao.findById(info.aid, info.bvid, info.cid)
            val list = mutableListOf<AsDownloadTask>()
            taskQueue.fold(list) { acc, element ->
                if (element.ids.cid == info.cid && element.ids.bvid == info.bvid && element.ids.aid == info.aid) {
                    acc.add(element)
                }
                list
            }
            if (list.size == 2 && list.all { it.state == State.COMPLETED }) {
                defaultGroupTaskCall.execute(tasks)
            }
        }
    }

    override fun progress(task: DownloadTask, currentOffset: Long, totalLength: Long) {
        scope.launch {
            Napier.d(tag = TAG) { "下载进度:$currentOffset--$totalLength ${task.filename} " }
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
