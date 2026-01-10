package com.imcys.bilibilias.ffmpeg

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object FFmpegManger {

    /**
     * A native method that is implemented by the 'ffmpeg' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    external fun getFFmpegVersion(): String

    interface FFmpegMergeListener {
        fun onProgress(progress: Int)
        fun onError(errorMsg: String)
        fun onComplete()
    }

    external fun mergeVideoAndAudio(
        videoPath: String,
        audioPath: String,
        outputPath: String,
        title: String,
        description: String,
        copyright: String,
        listener: FFmpegMergeListener
    )

    init {
        System.loadLibrary("ffmpeg")
    }

    external fun handleAppDownloadTask(
        taskJson: String,
        listener: FFmpegMergeListener
    )

    suspend fun handleAppDownloadTaskSuspend(
        taskJson: String,
        listener: FFmpegMergeListener
    ): Result<String> {
        return FFmpegExecutor.executeFFmpeg {
            runCatching {
                handleAppDownloadTask(
                    taskJson,
                    listener
                )
                "成功"
            }
        }
    }

    suspend fun mergeVideoAndAudioSuspend(
        videoPath: String,
        audioPath: String,
        outputPath: String,
        title: String,
        description: String,
        copyright: String,
        listener: FFmpegMergeListener
    ): Result<String> {
        return FFmpegExecutor.executeFFmpeg {
            runCatching {
                mergeVideoAndAudio(
                    videoPath,
                    audioPath,
                    outputPath,
                    title,
                    description,
                    copyright,
                    listener
                )
                "成功"
            }
        }
    }

    fun getVideoTempPath(
        context: Context,
        uri: Uri,
        cacheDir: String
    ): String? {
        // 确保缓存目录存在
        val cacheDirectory = File(cacheDir)
        if (!cacheDirectory.exists() && !cacheDirectory.mkdirs()) {
            throw IllegalStateException("无法创建缓存目录: $cacheDir")
        }

        val path = when (uri.scheme) {
            "file" -> uri.path!!
            "content" -> {
                val tempFile = File(cacheDirectory, "video_temp_${System.currentTimeMillis()}.tmp")
                tempFile.parentFile?.let { if (!it.exists()) it.mkdirs() }
                context.contentResolver.openInputStream(uri)?.use { input ->
                    FileOutputStream(tempFile).use { output ->
                        input.copyTo(output)
                    }
                } ?: error("无法打开内容 Uri: $uri")
                tempFile.absolutePath
            }

            else -> uri.path ?: uri.toString()
        }

        return path
    }

    interface FFmpegFrameListener {
        fun onFrame(data: ByteArray, width: Int, height: Int, index: Int)
        fun onProgress(progress: Int)
        fun onComplete()
    }

    external fun getVideoFramesJNI(
        videoPath: String,
        framesPerSecond: Int,
        listener: FFmpegFrameListener
    )

    external fun getVideoFrameRate(
        videoPath: String
    ): Int

    external fun checkSign(apkSign: String): Boolean
}
