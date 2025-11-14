package com.imcys.bilibilias.ui.download

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.FileProvider.getUriForFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.data.repository.DownloadTaskRepository
import com.imcys.bilibilias.database.entity.download.DownloadSegment
import com.imcys.bilibilias.download.DownloadManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import androidx.core.net.toUri

class DownloadViewModel(
    private val downloadManager: DownloadManager,
    private val downloadTaskRepository: DownloadTaskRepository,
    private val contentResolver: ContentResolver
) : ViewModel() {
    val downloadListState = downloadManager.getAllDownloadTasks()

    private val _allDownloadSegment = MutableStateFlow<List<DownloadSegment>>(emptyList())
    val allDownloadSegment = _allDownloadSegment

    init {
        viewModelScope.launch(Dispatchers.IO) {
            downloadTaskRepository.getSegmentAll().collect {
                allDownloadSegment.emit(it)
            }
        }
    }

    /**
     * 暂停下载任务
     * [segmentId] 下载任务的ID
     */
    fun pauseDownloadTask(segmentId: Long) {
        viewModelScope.launch { downloadManager.pauseTask(segmentId) }
    }

    @SuppressLint("MissingPermission")
    fun resumeDownloadTask(segmentId: Long) {
        viewModelScope.launch { downloadManager.resumeTask(segmentId) }
    }

    fun cancelDownloadTask(segmentId: Long) {
        viewModelScope.launch { downloadManager.cancelTask(segmentId) }
    }
    /**
     * 打开下载的文件
     * [context] 上下文
     * [segment] 下载任务
     */
    fun openDownloadSegmentFile(context: Context, segment: DownloadSegment) {
        // 文件地址
        val savePath = segment.savePath
        if (savePath.startsWith("content://")) {
            // content uri 直接打开
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val fileUri = savePath.toUri()
            val type = context.contentResolver.getType(fileUri) ?: ""
            intent.setDataAndType(fileUri, type)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(context, "无法打开此文件，可能没有合适的应用", Toast.LENGTH_SHORT)
                    .show()
            }
            return
        } else {
            // 普通文件路径
            val file = File(savePath)
            if (!file.exists()) {
                Toast.makeText(context, "文件不存在，可能已被删除", Toast.LENGTH_SHORT).show()
                return
            }
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val fileUri = try {
                getUriForFile(context, context.applicationContext.packageName + ".provider", file)
            } catch (e: Exception) {
                null
            }
            if (fileUri == null) {
                Toast.makeText(context, "无法打开此文件，可能没有合适的应用", Toast.LENGTH_SHORT)
                    .show()
                return
            }
            val type = context.contentResolver.getType(fileUri) ?: ""
            intent.setDataAndType(fileUri, type)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(context, "无法打开此文件，可能没有合适的应用", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    fun downloadSelectedTasks(segments: List<DownloadSegment>) {
        viewModelScope.launch(Dispatchers.IO) {
            segments.forEach { segment->
                runCatching {
                    val savePath = segment.savePath
                    if (savePath.startsWith("content://")) {
                        val uri = savePath.toUri()
                        contentResolver.delete(uri, null, null)
                    } else {
                        val file = File(savePath)
                        if (!file.exists()) {} else { file.delete() }
                    }
                }
                downloadTaskRepository.deleteSegment(segment.segmentId)
            }
        }
    }

    fun deleteDownloadSegment(context: Context, segment: DownloadSegment) {
        viewModelScope.launch(Dispatchers.IO) {
            var deleteSuccess = false
            var fileNotExist = false

            runCatching {
                val savePath = segment.savePath
                if (savePath.startsWith("content://")) {
                    val uri = savePath.toUri()
                    val rows = contentResolver.delete(uri, null, null)
                    deleteSuccess = rows > 0
                } else {
                    val file = File(savePath)
                    if (!file.exists()) {
                        fileNotExist = true
                    } else {
                        deleteSuccess = file.delete()
                    }
                }
            }.onFailure {
                deleteSuccess = false
            }

            downloadTaskRepository.deleteSegment(segment.segmentId)

            val message = when {
                fileNotExist -> "文件不存在"
                deleteSuccess -> "删除成功"
                else -> "删除失败，文件可能已经被删除或不存在"
            }

            launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}