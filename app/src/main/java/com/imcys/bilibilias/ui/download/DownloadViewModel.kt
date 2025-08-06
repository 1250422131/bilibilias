package com.imcys.bilibilias.ui.download

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.data.repository.DownloadTaskRepository
import com.imcys.bilibilias.database.entity.download.DownloadSegment
import com.imcys.bilibilias.dwonload.DownloadManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
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