package com.imcys.bilibilias.ui.download


import com.imcys.bilibilias.R
import androidx.compose.ui.res.stringResource
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
import com.imcys.bilibilias.dwonload.DownloadManager
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
                Toast.makeText(context, stringResource(R.string.download_none), Toast.LENGTH_SHORT)
                    .show()
            }
            return
        } else {
            // 普通文件路径
            val file = File(savePath)
            if (!file.exists()) {
                Toast.makeText(context, stringResource(R.string.download_delete_5), Toast.LENGTH_SHORT).show()
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
                Toast.makeText(context, stringResource(R.string.download_none), Toast.LENGTH_SHORT)
                    .show()
                return
            }
            val type = context.contentResolver.getType(fileUri) ?: ""
            intent.setDataAndType(fileUri, type)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(context, stringResource(R.string.download_none), Toast.LENGTH_SHORT)
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
                fileNotExist -> stringResource(R.string.download_text_6)
                deleteSuccess -> stringResource(R.string.download_delete_1)
                else -> stringResource(R.string.download_delete_2)
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