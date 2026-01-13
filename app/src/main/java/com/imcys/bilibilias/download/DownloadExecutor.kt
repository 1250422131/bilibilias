package com.imcys.bilibilias.download

import com.imcys.bilibilias.data.repository.AppSettingsRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.head
import io.ktor.client.request.header
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.contentLength
import io.ktor.utils.io.exhausted
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * 下载执行器
 * 负责执行HTTP下载，支持断点续传和重试
 */
class DownloadExecutor(
    private val httpClient: HttpClient,
    private val appSettingsRepository: AppSettingsRepository
) {
    companion object {
        private const val MAX_RETRY_ATTEMPTS = 5
        private const val RETRY_DELAY_MS = 3000L
        private const val DOWNLOAD_BUFFER_SIZE : Long = 64 * 1024
    }

    /**
     * 下载文件到指定路径
     * @param downloadUrl 下载URL
     * @param savePath 保存路径
     * @param referer Referer头
     * @param onProgress 进度回调 (0.0 - 1.0)
     * @return 是否下载成功
     */
    suspend fun downloadFile(
        downloadUrl: String,
        savePath: String,
        referer: String,
        onProgress: (Float) -> Unit
    ): Boolean = withContext(Dispatchers.IO) {
        val file = File(savePath)
        val tempFile = File("$savePath.downloading")

        // 检查文件是否已完整下载
        val remoteLength = getRemoteFileLength(downloadUrl, referer)
        if (remoteLength > 0 && file.exists() && file.length() >= remoteLength) {
            onProgress(1f)
            return@withContext true
        }

        // 重试下载
        repeat(MAX_RETRY_ATTEMPTS) { attempt ->
            try {
                val success = performDownload(downloadUrl, tempFile, referer, onProgress)
                if (success) {
                    if (file.exists()) file.delete()
                    tempFile.renameTo(file)
                    return@withContext true
                }
            } catch (e: Exception) {
                if (attempt < MAX_RETRY_ATTEMPTS - 1) {
                    delay(RETRY_DELAY_MS)
                }
            }
        }
        false
    }

    /**
     * 获取远程文件长度
     */
    private suspend fun getRemoteFileLength(url: String, referer: String): Long {
        return try {
            val headResp = httpClient.head(url) {
                header("Referer", referer)
            }
            headResp.headers["Content-Length"]?.toLongOrNull() ?: -1L
        } catch (e: Exception) {
            -1L
        }
    }

    private suspend fun performDownload(
        downloadUrl: String,
        tempFile: File,
        referer: String,
        onProgress: (Float) -> Unit,
        progressStep: Float = 0.01f
    ): Boolean = withContext(Dispatchers.IO) {
        val downloaded = if (tempFile.exists()) tempFile.length() else 0L
        val finalUrl = replaceCdn(downloadUrl)
        var lastProgress = 0f

        try {
            httpClient.prepareGet(finalUrl) {
                header("Referer", referer)
                if (downloaded > 0) header("Range", "bytes=$downloaded-")
            }.execute { response ->
                tempFile.parentFile?.mkdirs()
                val channel = response.bodyAsChannel()

                val contentLength = response.contentLength()
                val totalLength = if (contentLength != null && contentLength > 0) {
                    contentLength + downloaded
                } else {
                    -1L
                }

                val buffer = ByteArray(DOWNLOAD_BUFFER_SIZE.toInt())
                var downloadedBytes = downloaded

                FileOutputStream(tempFile, downloaded > 0).use { output ->
                    while (!channel.exhausted()) {
                        val bytesRead = channel.readAvailable(buffer)
                        if (bytesRead == -1) break

                        output.write(buffer, 0, bytesRead)
                        downloadedBytes += bytesRead

                        val currentProgress = if (totalLength > 0) {
                            (downloadedBytes.toFloat() / totalLength.toFloat()).coerceIn(0f, 1f)
                        } else {
                            0f
                        }

                        // 只在进度变化超过指定步长时回调
                        if (currentProgress - lastProgress >= progressStep) {
                            lastProgress = currentProgress
                            onProgress(currentProgress)
                        }
                    }
                }

                onProgress(1f)
                true
            }
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 替换CDN
     */
    private suspend fun replaceCdn(downloadUrl: String): String {
        val lineHost = appSettingsRepository.appSettingsFlow.first().biliLineHost ?: ""
        val uposRegex = Regex("""upos-sz-estg[0-9a-z]*\.bilivideo\.com""", RegexOption.IGNORE_CASE)
        return if (uposRegex.containsMatchIn(downloadUrl) && lineHost.isNotEmpty()) {
            downloadUrl.replace(uposRegex, lineHost)
        } else {
            downloadUrl
        }
    }
}
