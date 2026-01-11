package com.imcys.bilibilias.download

import com.imcys.bilibilias.data.repository.AppSettingsRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.head
import io.ktor.client.request.header
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.contentLength
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.io.File
import java.io.RandomAccessFile

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
        private const val DOWNLOAD_BUFFER_SIZE = 64 * 1024
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

    /**
     * 执行实际下载
     */
    private suspend fun performDownload(
        downloadUrl: String,
        tempFile: File,
        referer: String,
        onProgress: (Float) -> Unit
    ): Boolean = withContext(Dispatchers.IO) {
        val downloaded = if (tempFile.exists()) tempFile.length() else 0L
        val finalUrl = replaceCdn(downloadUrl)

        httpClient.prepareGet(finalUrl) {
            header("Referer", referer)
            if (downloaded > 0) header("Range", "bytes=$downloaded-")
        }.execute { response ->
            val channel = response.bodyAsChannel()
            val total = response.contentLength()?.let {
                if (downloaded > 0) it + downloaded else it
            } ?: -1L

            if (total == 0L) {
                onProgress(1f)
                return@execute true
            }

            tempFile.parentFile?.mkdirs()
            val raf = RandomAccessFile(tempFile, "rw")
            if (downloaded > 0) raf.seek(downloaded)

            var currentDownloaded = downloaded
            val buffer = ByteArray(DOWNLOAD_BUFFER_SIZE)
            var lastEmit = 0L

            try {
                while (!channel.isClosedForRead) {
                    ensureActive()
                    val bytes = channel.readAvailable(buffer, 0, buffer.size)
                    if (bytes <= 0) break

                    raf.write(buffer, 0, bytes)
                    currentDownloaded += bytes

                    val now = System.currentTimeMillis()
                    if (now - lastEmit > 100) {
                        if (total > 0) onProgress(currentDownloaded.toFloat() / total)
                        lastEmit = now
                    }
                    yield()
                }
                if (total > 0) onProgress(1f)
            } finally {
                raf.close()
            }
        }
        true
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
