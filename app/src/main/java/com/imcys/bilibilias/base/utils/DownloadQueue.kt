package com.imcys.bilibilias.base.utils

import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.base.model.user.DownloadTaskDataBean
import org.xutils.common.Callback
import org.xutils.common.task.PriorityExecutor
import org.xutils.http.RequestParams
import org.xutils.x
import java.io.File


// 定义一个下载队列类
class DownloadQueue {
    // 定义一个队列用来存储待下载的任务
    private val queue = mutableListOf<Task>()

    // 定义一个变量，用来保存当前正在下载的任务
    private var currentTask: Task? = null

    // 定义一个内部类，表示下载任务
    private inner class Task(
        // 定义下载地址
        val url: String,
        // 定义下载文件保存路径
        val savePath: String,
        //定义下载任务的其他参撒
        val downloadTaskDataBean: DownloadTaskDataBean,
        // 定义下载完成回调
        val onComplete: (Boolean) -> Unit,
        // 定义当前任务的下载进度
        var progress: Double = 0.0,
        //定义当前文件大小
        var fileSize: Double = 0.0,
        //定义当前已经下载的大小
        var fileDlSize: Double = 0.0,
        // 定义当前任务的下载请求
        var call: Callback.Cancelable? = null,
    )

    // 定义一个函数，用来添加下载任务到队列中
    fun addTask(
        url: String,
        savePath: String,
        downloadTaskDataBean: DownloadTaskDataBean,
        onComplete: (Boolean) -> Unit,
    ) {
        // 创建一个下载任务
        val task = Task(url, savePath, downloadTaskDataBean, onComplete)
        // 添加下载任务到队列中
        queue.add(task)

        // 如果队列中只有这一个任务，那么就执行这个任务
        if (queue.size == 1) {
            executeTask(task)
        }
    }


    // 定义一个函数，用来执行下载任务
    private fun executeTask(task: Task) {
        //刷新下载对象
        currentTask = task
        // 创建一个 RequestParams 对象，用来指定下载地址和文件保存路径
        val params = RequestParams(task.url)
        //设置header头
        params.addHeader("User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0");
        params.addHeader("referer", "https://www.bilibili.com/")
        params.addHeader("cookie", App.cookies)
        //设置是否根据头信息自动命名文件
        params.isAutoRename = false
        //设储存路径
        params.saveFilePath = task.savePath
        //自定义线程池,有效的值范围[1, 3], 设置为3时, 可能阻塞图片加载.
        params.executor = PriorityExecutor(1, true)
        //是否可以被立即停止.
        params.isCancelFast = true


        // 使用 XUtils 库来下载文件
        val call = x.http().get(params, object : Callback.ProgressCallback<File> {
            override fun onSuccess(result: File?) {
                // 下载成功，调用任务的完成回调
                task.onComplete(true)
                // 执行下一个任务
                nextTask()
            }

            override fun onError(ex: Throwable?, isOnCallback: Boolean) {
                // 下载失败，调用任务的完成回调
                task.onComplete(false)
                // 执行下一个任务
                nextTask()
            }

            override fun onCancelled(cex: Callback.CancelledException?) {
                // 下载取消，调用任务的完成回调
                task.onComplete(false)
                // 执行下一个任务
                nextTask()
            }

            override fun onFinished() {
                // 不需要实现
            }

            override fun onWaiting() {
                // 不需要实现
            }

            override fun onStarted() {

            }

            override fun onLoading(total: Long, current: Long, isDownloading: Boolean) {
                task.progress = (current * 100 / total).toDouble()
                task.fileSize = (total / 1048576).toDouble()
                task.fileDlSize = (current / 1048576).toDouble()
            }

        })

        task.call = call


    }

    // 定义一个函数，用来执行下一个任务
    private fun nextTask() {

        // 从队列中移除当前任务
        queue.removeAt(0)
        // 如果队列中还有任务，那么就执行下一个任务
        if (queue.isNotEmpty()) {
            executeTask(queue[0])
        }
    }


}