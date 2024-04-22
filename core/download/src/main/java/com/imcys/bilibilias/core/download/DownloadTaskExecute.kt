package com.imcys.bilibilias.core.download

import com.imcys.bilibilias.core.download.task.AsDownloadTask
import com.imcys.bilibilias.core.download.task.AudioTask
import com.imcys.bilibilias.core.download.task.VideoTask
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.ViewInfo
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.core.Util
import com.liulishuo.okdownload.kotlin.listener.createListener1
import io.github.aakira.napier.Napier
import javax.inject.Inject

class DownloadTaskExecute @Inject constructor() {
    init {
        if (BuildConfig.DEBUG) {
            Util.enableConsoleLog()
        }
    }

    fun enqueue(
        videoTask: VideoTask,
        audioTask: AudioTask,
        onEnd: TaskEnd
    ) {
        Napier.d { videoTask.toString() + '\n' + audioTask.toString() }
        val listener = createListener1(
            taskStart = { task, model ->
                Napier.d { "任务开始 $task" }
            },
            taskEnd = { task, cause, realCause, model ->
                val info = AsDownloadTask.getDownloadInfo(task)
                onEnd(info.first, info.second)
            }
        )
        DownloadTask.enqueue(arrayOf(videoTask.task, audioTask.task), listener)
    }

    fun enqueue(task: AsDownloadTask, onEnd: TaskEnd) {
        Napier.d { task.toString() }
        when (task) {
            is AudioTask -> task.executeTask(onEnd)
            is VideoTask -> task.executeTask(onEnd)
        }
    }
}
internal typealias TaskEnd = (viewinfo: ViewInfo, type: FileType) -> Unit
