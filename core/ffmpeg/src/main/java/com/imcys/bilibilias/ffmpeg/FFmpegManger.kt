package com.imcys.bilibilias.ffmpeg

import android.content.Context
import android.net.Uri
import android.util.Pair
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

    fun getVideoFrames(
        videoPath: String,
        cacheDir: String
    ): Pair<ArrayList<String>, Int> {
        return Pair(arrayListOf(), 0)
    }

    init {
        System.loadLibrary("ffmpeg")
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

    fun getVideoFramesCompat(
        context: Context,
        uri: Uri,
        cacheDir: String
    ): Pair<ArrayList<String>, Int> {
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

        return getVideoFrames(path, cacheDirectory.absolutePath)
    }

}
