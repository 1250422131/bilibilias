package com.imcys.bilibilias.core.download

import androidx.collection.mutableObjectListOf
import androidx.core.net.toUri
import com.imcys.bilibilias.core.common.network.di.ApplicationScope
import com.imcys.bilibilias.core.data.toast.AsToastState
import com.imcys.bilibilias.core.data.toast.AsToastType
import com.imcys.bilibilias.core.data.toast.ToastMachine
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
import kotlinx.datetime.Clock
import java.lang.Exception
import javax.inject.Inject

class AsDownloadListener @Inject constructor(
    @ApplicationScope private val scope: CoroutineScope,
    private val downloadTaskDao: DownloadTaskDao,
    private val defaultGroupTaskCall: DefaultGroupTaskCall,
    private val toastMachine: ToastMachine
) : DownloadListener1() {
    private val taskQueue = mutableObjectListOf<AsDownloadTask>()

    fun add(task: AsDownloadTask) {
        taskQueue.add(task)
    }

    override fun taskStart(task: DownloadTask, model: Listener1Assist.Listener1Model) {
        Napier.d(tag = "taskStart") { "任务开始 $task" }
        val asTask = taskQueue.first { it.okTask === task }
        scope.launch {
            val info = asTask.viewInfo
            val taskEntity = DownloadTaskEntity(
                uri = asTask.destFile.toUri(),
                created = Clock.System.now(),
                aid = info.aid,
                bvid = info.bvid,
                cid = info.cid,
                fileType = asTask.fileType,
                subTitle = asTask.subTitle,
                title = info.title,
                state = State.RUNNING,
            )
            downloadTaskDao.insertOrUpdate(taskEntity)
        }
    }

    override fun taskEnd(
        task: DownloadTask,
        cause: EndCause,
        realCause: Exception?,
        model: Listener1Assist.Listener1Model
    ) {
        Napier.d(tag = "taskEnd", throwable = realCause) { "任务结束 $cause-${task.file?.path}" }
        val asDownloadTask = taskQueue.first { it.okTask === task }
        toast(realCause, asDownloadTask)

        scope.launch {

            downloadTaskDao.updateState(
                task.uri,
                if (realCause == null) State.COMPLETED else State.ERROR
            )
            val info = asDownloadTask.viewInfo
            val tasks = downloadTaskDao.getTaskByInfo(info.aid, info.bvid, info.cid)
            val v = tasks.find { it.fileType == FileType.VIDEO }
            val a = tasks.find { it.fileType == FileType.AUDIO }
            if (v != null && v.state == State.COMPLETED) {
                if (a != null && a.state == State.COMPLETED) {
                    defaultGroupTaskCall.execute(GroupTask(v, a))
                }
            }
        }
    }

    private fun toast(
        realCause: Exception?,
        task: AsDownloadTask
    ) {
        val toastState = if (realCause == null) {
            AsToastState("${task.subTitle}·${task.fileType}下载成功", AsToastType.Normal)
        } else {
            AsToastState("${task.subTitle}·${task.fileType}下载失败", AsToastType.Error)
        }
        toastMachine.show(toastState)
    }

    override fun progress(task: DownloadTask, currentOffset: Long, totalLength: Long) {
        Napier.d(tag = "progress") { "任务中 ${task.filename} $currentOffset-$totalLength" }
        scope.launch {
            downloadTaskDao.updateProgressAndState(
                task.uri,
                State.RUNNING,
                currentOffset,
                totalLength
            )
        }
    }

    override fun retry(task: DownloadTask, cause: ResumeFailedCause) = Unit

    override fun connected(
        task: DownloadTask,
        blockCount: Int,
        currentOffset: Long,
        totalLength: Long
    ) = Unit
}
