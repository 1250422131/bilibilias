package com.imcys.bilibilias.core.network.download

import com.imcys.bilibilias.core.common.network.di.ApplicationScope
import com.imcys.bilibilias.core.network.repository.VideoRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val MAX_RUNNING = 4

class Downloader @Inject constructor(
    private val videoRepository: VideoRepository,
    @ApplicationScope private val scope: CoroutineScope,
) {
    private val ready = ArrayDeque<Task>(MAX_RUNNING)
    private val running = ArrayDeque<Task>(MAX_RUNNING)
    private val completed = ArrayDeque<Task>()

    fun dispatch(task: Task) {
        ready.add(task)
        promoteAndExecute()
    }

    private fun promoteAndExecute() {
        val i = ready.iterator()
        while (i.hasNext() && running.size < MAX_RUNNING) {
            val task = i.next()
            running.add(task)
            execute(task)
            i.remove()
        }
    }

    private fun execute(task: Task) {
        task.job = scope.launch {
            videoRepository.downloader(task.url, task.path)
                .buffer(onBufferOverflow = BufferOverflow.DROP_OLDEST).collect {
                    when (it) {
                        is DownloadResult.Error -> {
                            Napier.d { "下载错误 ${it.message} " }
                            finished(task)
                        }

                        is DownloadResult.Progress -> {
                            task.progress = it.percent
                            Napier.d { "下载进度 ${it.percent}" }
                        }

                        DownloadResult.Success -> {
                            finished(task)
                            Napier.d { "下载进度成功" }
                        }
                    }
                }
        }
    }

    private fun finished(task: Task) {
        finished(running, task)
    }

    private fun finished(deque: ArrayDeque<Task>, task: Task) {
        if (!deque.remove(task)) throw AssertionError("运行队列中没有的任务")
        completed.add(task)
        promoteAndExecute()
    }
}
