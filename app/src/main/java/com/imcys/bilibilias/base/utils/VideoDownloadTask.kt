package com.imcys.bilibilias.base.utils

import android.icu.text.CaseMap.Title
import com.imcys.bilibilias.base.api.BilibiliApi
import com.imcys.bilibilias.base.model.user.DownloadTaskBean
import com.imcys.bilibilias.utils.HttpUtils
import org.xutils.common.Callback
import java.io.File


/**
 * 下载任务操作单例类
 */
object VideoDownloadTask {

    private lateinit var downloadTaskBean: DownloadTaskBean
    var taskState = false
    lateinit var errorMethod: (ex: Throwable?, isOnCallback: Boolean) -> Boolean
    lateinit var finishMethod: (result: File?) -> Boolean


    /**
     * 启动任务
     * @param downloadTaskBean DownloadTaskBean 任务清单类
     */
    fun startTask(
        downloadTaskBean: DownloadTaskBean,
        errorMethod: (ex: Throwable?, isOnCallback: Boolean) -> Boolean,
        finishMethod: (result: File?) -> Boolean,
    ) {
        this.errorMethod = errorMethod
        this.finishMethod = finishMethod
        this.downloadTaskBean = downloadTaskBean
        inspectTask()
    }

    /**
     * 检阅任务
     */
    private fun inspectTask() {

        val taskList = downloadTaskBean.videoTasks

        //判空拦截
        if (taskList.isEmpty()) return
        if (taskList.size == 0) return

        val taskData = taskList[0]
        val bvid = taskData.videoBaseBean.data.bvid

        HttpUtils.downLoadFile(BilibiliApi.videoPlayPath + "?bvid=$bvid&qn=${taskData.qn}&fnval=${taskData.fnval}&fourk=1&platform=${taskData.platform}",
            object : Callback.ProgressCallback<File> {
                override fun onSuccess(result: File?) {
                    if (finishMethod(result)) {
                        taskList.removeAt(0)
                        inspectTask()
                    }
                }

                override fun onError(ex: Throwable?, isOnCallback: Boolean) {
                    if (errorMethod(ex, isOnCallback)) {
                        taskList.removeAt(0)
                        inspectTask()
                    }
                }

                override fun onCancelled(cex: Callback.CancelledException?) {

                }

                override fun onFinished() {

                }

                override fun onWaiting() {

                }

                override fun onStarted() {

                }

                override fun onLoading(total: Long, current: Long, isDownloading: Boolean) {
                    //判断下载状态
                    if (isDownloading) {
                        val progress = (current * 100 / total).toInt()
                        val videoFileSize = (total / 1048576).toDouble()
                        val videoFileDlSize = (current / 1048576).toDouble()
                        //状态更新
                        taskData.downloadProgressData.fileSize = videoFileSize
                        taskData.downloadProgressData.fileDlSize = videoFileDlSize
                        taskData.downloadProgressData.progress = progress
                    }
                }

            })

    }


}