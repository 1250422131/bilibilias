package com.imcys.bilibilias.core.download

import androidx.collection.MutableObjectList
import androidx.collection.mutableLongObjectMapOf
import com.imcys.bilibilias.core.download.extension.enqueue
import com.imcys.bilibilias.core.download.task.AsDownloadTask
import com.imcys.bilibilias.core.download.task.Audio
import com.imcys.bilibilias.core.download.task.Video
import com.liulishuo.okdownload.DownloadTask
import java.io.File
import javax.inject.Inject

class DownloadTaskSystem @Inject constructor() {
    // 放到上一层
    private val taskQueue = mutableLongObjectMapOf<MutableObjectList<AsDownloadTask>>()

//    val values = taskQueue.getOrPut(task.viewInfo.cid) { mutableObjectListOf() }
//    values.add(task)
    fun enqueue(task: AsDownloadTask, onEnd: (Long) -> Unit) {
        when (task) {
            is Audio -> createTask(task.url, task.destFile, task.priority, task.viewInfo.cid)
                .also {
                    task.task = it
                    it.enqueue(
                        taskEnd = { task, cause, realCause, model ->
                            onEnd(task.tag as Long)
                        }
                    )
                }

            is Video -> createTask(task.url, task.destFile, task.priority, task.viewInfo.cid)
                .also {
                    task.task = it
                    it.enqueue(
                        taskEnd = { task, cause, realCause, model ->
                            onEnd(task.tag as Long)
                        }
                    )
                }
        }
    }
    private fun createTask(
        url: String,
        file: File,
        priority: Int,
        cid: Long
    ): DownloadTask {
        return DownloadTask.Builder(url, file)
            .setPassIfAlreadyCompleted(true)
            .setAutoCallbackToUIThread(false)
            .setHeaderMapFields(
                mapOf(
                    "User-Agent" to
                        listOf("Mozilla/4.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/70.0.3538.77 Chrome/70.0.3538.77 Safari/537.36"),
                    "Referer" to listOf("https://www.bilibili.com/")
                )
            )
            .setPriority(priority)
            .build()
            .apply {
                tag = cid
            }
    }
}
