package com.imcys.bilibilias.download

import android.app.Application
import android.util.Log
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFprobeKit
import com.arthenica.ffmpegkit.ReturnCode
import com.imcys.bilibilias.database.entity.download.MediaContainer
import com.imcys.bilibilias.data.model.download.DownloadSubTask
import com.imcys.bilibilias.data.model.download.MediaContainerConfig
import com.imcys.bilibilias.database.entity.download.DownloadMode
import com.imcys.bilibilias.database.entity.download.DownloadSubTaskType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * FFmpeg合并器
 * 负责视频/音频合并和字幕/封面嵌入
 */
class FfmpegMerger(
    private val context: Application
) {

    /**
     * 合并视频和音频
     */
    suspend fun mergeMedia(
        subTasks: List<DownloadSubTask>,
        downloadMode: DownloadMode,
        outputFile: File,
        subtitles: List<LocalSubtitle> = emptyList(),
        coverPath: String = "",
        duration: Long? = null,
        onProgress: (Float) -> Unit,
        mediaContainerConfig: MediaContainerConfig
    ) {
        val command = buildFfmpegCommand(
            subTasks,
            downloadMode,
            outputFile,
            subtitles,
            coverPath,
            mediaContainerConfig,
        )

        val actualDuration = duration ?: getMediaDuration(subTasks.firstOrNull()?.savePath)

        executeFfmpegCommand(command, outputFile, actualDuration, onProgress)
    }

    /**
     * 获取媒体时长
     */
    private suspend fun getMediaDuration(filePath: String?): Long {
        return suspendCancellableCoroutine { continuation ->
            if (filePath.isNullOrBlank()) {
                continuation.resume(0L)
                return@suspendCancellableCoroutine
            }

            try {
                val mediaInfoSession = FFprobeKit.getMediaInformation(filePath)

                if (ReturnCode.isSuccess(mediaInfoSession.returnCode)) {
                    val mediaInfo = mediaInfoSession.mediaInformation
                    if (mediaInfo != null) {
                        val durationInSeconds = mediaInfo.duration?.toDoubleOrNull() ?: 0.0
                        val durationInMillis = (durationInSeconds * 1000).toLong()
                        continuation.resume(durationInMillis)
                    } else {
                        continuation.resume(0L)
                    }
                } else {
                    Log.w("FFmpeg", "FFprobe failed: ${mediaInfoSession.returnCode}")
                    continuation.resume(0L)
                }
            } catch (e: Exception) {
                continuation.resume(0L)
            }
        }
    }

    /**
     * 执行FFmpeg命令
     */
    private suspend fun executeFfmpegCommand(
        command: String,
        outputFile: File,
        duration: Long,
        onProgress: (Float) -> Unit
    ) {
        suspendCancellableCoroutine { continuation ->
            var lastProgressEmit = 0L

            val session = FFmpegKit.executeAsync(
                command,
                { session ->
                    when {
                        ReturnCode.isSuccess(session.returnCode) -> {
                            if (!outputFile.exists() || outputFile.length() == 0L) {
                                continuation.resumeWithException(Exception("输出文件生成失败"))
                            } else {
                                Log.d("FFmpeg", "合并完成: ${outputFile.absolutePath}")
                                continuation.resume(Unit)
                            }
                        }

                        ReturnCode.isCancel(session.returnCode) -> {
                            outputFile.deleteIfExists()
                            Log.w("FFmpeg", "任务被取消")
                            continuation.resumeWithException(CancellationException("任务被取消"))
                        }

                        else -> {
                            outputFile.deleteIfExists()
                            Log.e("FFmpeg", "执行失败: ${session.failStackTrace}")
                            continuation.resumeWithException(
                                Exception("FFmpeg执行失败: ${session.failStackTrace}")
                            )
                        }
                    }
                },
                { log ->
                    Log.d("FFmpeg", "${log}")
                },
                { statistics ->
                    if (statistics.time > 0 && duration > 0) {
                        val now = System.currentTimeMillis()
                        if (now - lastProgressEmit > 100) {
                            val progress = (statistics.time.toFloat() / duration.toFloat()).coerceIn(0f, 1f)
                            onProgress(progress)
                            lastProgressEmit = now
                        }
                    }
                }
            )

            continuation.invokeOnCancellation {
                Log.w("FFmpeg", "协程取消，停止FFmpeg会话")
                FFmpegKit.cancel(session.sessionId)
            }
        }
    }

    /**
     * 构建FFmpeg命令
     */
    private fun buildFfmpegCommand(
        subTasks: List<DownloadSubTask>,
        downloadMode: DownloadMode,
        outputFile: File,
        subtitles: List<LocalSubtitle>,
        coverPath: String,
        mediaContainerConfig: MediaContainerConfig
    ): String {
        val mediaInputs = subTasks.map { it.savePath }
        val videoFileCount = subTasks.count { it.subTaskType == DownloadSubTaskType.VIDEO }
        val audioFileCount = subTasks.count { it.subTaskType == DownloadSubTaskType.AUDIO }

        val videoEnabled = downloadMode in listOf(DownloadMode.VIDEO_ONLY, DownloadMode.AUDIO_VIDEO)
        val audioEnabled = downloadMode in listOf(DownloadMode.AUDIO_ONLY, DownloadMode.AUDIO_VIDEO)

        val audioFileStartIdx = videoFileCount
        val subFileStartIdx = mediaInputs.size
        val coverIdx = if (coverPath.isNotBlank()) mediaInputs.size + subtitles.size else -1

        return buildList {
            // 基础参数
            add("-y")
            add("-strict")
            add("-2")

            // 输入文件
            mediaInputs.forEach {
                add("-i")
                add(it)
            }
            subtitles.forEach {
                add("-i")
                add(it.path)
            }
            if (coverIdx >= 0) {
                add("-i")
                add(coverPath)
            }

            // 流映射
            if (videoEnabled) {
                add("-map")
                add("0:v:0")
            }
            if (audioEnabled) {
                repeat(audioFileCount) { i ->
                    add("-map")
                    add("${audioFileStartIdx + i}:a:$i")
                }
            }
            subtitles.forEachIndexed { sIdx, _ ->
                add("-map")
                add("${subFileStartIdx + sIdx}:s:0")
            }
            if (coverIdx >= 0) {
                add("-map")
                add("$coverIdx:v:0")
            }

            // 编解码器
            if (videoEnabled) {
                add("-c:v")
                add("copy")
            }
            if (audioEnabled) {
                add("-c:a")
                add("copy")
            }

            // 字幕元数据
            if (subtitles.isNotEmpty() && videoEnabled) {
                add("-c:s")
                if (mediaContainerConfig.videoContainer != MediaContainer.MKV){
                    add("mov_text")
                } else {
                    add("copy")
                }
                subtitles.forEachIndexed { sIdx, subtitle ->
                    add("-metadata:s:s:$sIdx")
                    add("language=${subtitle.lang}")
                    add("-metadata:s:s:$sIdx")
                    add("title=${subtitle.langDoc}")
                }
            }

            // 封面配置
            if (coverIdx >= 0) {
                val coverStreamIndex = if (videoEnabled) 1 else 0
                add("-c:v:$coverStreamIndex")
                add("mjpeg")
                add("-disposition:v:$coverStreamIndex")
                add("attached_pic")
                add("-metadata:s:v:$coverStreamIndex")
                add("title=Cover")
            }

            if (audioEnabled && mediaContainerConfig.audioContainer == MediaContainer.MP3) {
                addAll(listOf("-codec:a", "libmp3lame", "-q:a","2"))
            }

            add(outputFile.absolutePath)
        }.joinToString(" ") { it.escape() }.also {
            Log.d("FFmpeg", "执行命令: $it")
        }
    }

    private fun String.escape(): String = when {
        contains(" ") || contains("\"") || contains("'") -> {
            "\"${replace("\"", "\\\"")}\""
        }
        else -> this
    }

    private fun File.deleteIfExists() {
        if (exists()) delete()
    }
}
