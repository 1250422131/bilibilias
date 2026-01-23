package com.imcys.bilibilias.download

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentUris
import android.content.ContentValues
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

/**
 * 文件输出管理器
 * 负责处理所有文件输出操作和MediaStore注册
 */
class FileOutputManager(
    private val context: Application
) {
    /**
     * 创建字幕输出流
     */
    fun createSubtitleOutputStream(fileName: String, type: SubtitleType): OutputStream {
        val mime = when (type) {
            SubtitleType.ASS -> "text/x-ass"
            SubtitleType.SRT -> "application/x-subrip"
        }
        return createDownloadOutputStream(fileName, mime, "BILIBILIAS")
    }

    /**
     * 创建弹幕输出流
     */
    suspend fun createDanmakuOutputStream(fileName: String): OutputStream =
        withContext(Dispatchers.IO) {
            createDownloadOutputStream(fileName, "application/xml", "BILIBILIAS/Danmaku")
        }

    /**
     * 下载图片到相册
     */
    suspend fun downloadImageToAlbum(
        imageBytes: ByteArray,
        fileName: String,
        saveDirName: String
    ) = withContext(Dispatchers.IO) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveImageMediaStore(imageBytes, fileName, saveDirName)
        } else {
            saveImageLegacy(imageBytes, fileName, saveDirName)
        }
    }

    /**
     * 将文件移动到下载目录并注册到媒体库
     */
    suspend fun moveToDownloadAndRegister(
        file: File,
        fileName: String,
        mimeType: String
    ): String? = withContext(Dispatchers.IO) {
        val parts = fileName.split("/")
        val actualFileName = parts.last()
        val folderPath = if (parts.size > 1) parts.dropLast(1).joinToString("/") else ""
        val relativePath = if (folderPath.isNotEmpty())
            "${Environment.DIRECTORY_DOWNLOADS}/BILIBILIAS/$folderPath"
        else
            "${Environment.DIRECTORY_DOWNLOADS}/BILIBILIAS"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            moveToDownloadMediaStore(file, actualFileName, relativePath, mimeType)
        } else {
            moveToDownloadLegacy(file, actualFileName, folderPath)
        }
    }

    // Private helper methods

    private fun createDownloadOutputStream(
        fileName: String,
        mimeType: String,
        relativePath: String
    ): OutputStream {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val uri = context.contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                    put(MediaStore.Downloads.MIME_TYPE, mimeType)
                    put(MediaStore.Downloads.RELATIVE_PATH, "Download/$relativePath")
                }
            ) ?: throw Exception("MediaStore insert failed")
            context.contentResolver.openOutputStream(uri)
        } else {
            val dir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                relativePath
            ).apply { mkdirs() }
            FileOutputStream(File(dir, fileName))
        }.let { checkNotNull(it) { "OutputStream == null" } }
    }

    private fun saveImageMediaStore(imageBytes: ByteArray, fileName: String, saveDirName: String) {
        val resolver = context.contentResolver
        val relativeRoot = Environment.DIRECTORY_PICTURES
        val relativePath = "$relativeRoot/$saveDirName"

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/${fileName.substringAfterLast('.')}")
            put(MediaStore.Images.Media.RELATIVE_PATH, relativePath)
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        uri?.let {
            try {
                resolver.openOutputStream(it)?.use { out ->
                    out.write(imageBytes)
                    out.flush()
                }
            } catch (e: Exception) {
                runCatching { resolver.delete(it, null, null) }
                throw e
            } finally {
                val update = ContentValues().apply { put(MediaStore.Images.Media.IS_PENDING, 0) }
                runCatching { resolver.update(it, update, null, null) }
            }
        }
    }

    private fun saveImageLegacy(imageBytes: ByteArray, fileName: String, saveDirName: String) {
        val baseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            .absolutePath + "/$saveDirName"
        val albumDir = File(baseDir).apply { if (!exists()) mkdirs() }
        val outFile = File(albumDir, fileName)

        FileOutputStream(outFile).use { out ->
            out.write(imageBytes)
            out.flush()
        }

        MediaScannerConnection.scanFile(
            context,
            arrayOf(outFile.absolutePath),
            arrayOf("image/${fileName.substringAfterLast('.')}"),
            null
        )
    }

    private fun moveToDownloadMediaStore(
        file: File,
        fileName: String,
        relativePath: String,
        mimeType: String
    ): String? {
        val resolver = context.contentResolver
        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, mimeType)
            put(MediaStore.Downloads.RELATIVE_PATH, relativePath)
        }

        // 删除已存在的同名文件
        val query = MediaStore.Downloads.EXTERNAL_CONTENT_URI
        val selection =
            "${MediaStore.Downloads.DISPLAY_NAME}=? AND ${MediaStore.Downloads.RELATIVE_PATH}=?"
        val selectionArgs = arrayOf(fileName, relativePath)
        resolver.query(query, arrayOf(MediaStore.Downloads._ID), selection, selectionArgs, null)
            ?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Downloads._ID))
                    val existUri =
                        ContentUris.withAppendedId(MediaStore.Downloads.EXTERNAL_CONTENT_URI, id)
                    resolver.delete(existUri, null, null)
                }
            }

        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
        return if (uri != null) {
            try {
                resolver.openOutputStream(uri)?.use { outputStream ->
                    file.inputStream().use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                file.delete()
                uri.toString()
            } catch (e: Exception) {
                null
            }
        } else null
    }

    private fun moveToDownloadLegacy(
        file: File,
        fileName: String,
        folderPath: String
    ): String? {
        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        var targetDir = File(downloadsDir, "BILIBILIAS")
        if (!targetDir.exists()) targetDir.mkdirs()

        if (folderPath.isNotEmpty()) {
            folderPath.split("/").forEach { part ->
                targetDir = File(targetDir, part)
                if (!targetDir.exists()) targetDir.mkdirs()
            }
        }

        val targetFile = File(targetDir, fileName)
        return try {
            file.inputStream().use { inputStream ->
                targetFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            file.delete()
            targetFile.absolutePath
        } catch (e: Exception) {
            null
        }
    }

    enum class SubtitleType {
        ASS, SRT
    }
}
