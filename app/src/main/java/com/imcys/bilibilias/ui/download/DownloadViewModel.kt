package com.imcys.bilibilias.ui.download

import android.annotation.SuppressLint
import android.content.ContentResolver
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.data.repository.AppSettingsRepository
import com.imcys.bilibilias.data.repository.DownloadTaskRepository
import com.imcys.bilibilias.database.entity.download.DownloadSegment
import com.imcys.bilibilias.database.entity.download.DownloadState
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.download.NewDownloadManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

class DownloadViewModel(
    private val downloadManager: NewDownloadManager,
    private val downloadTaskRepository: DownloadTaskRepository,
    private val contentResolver: ContentResolver,
    private val appSettingsRepository: AppSettingsRepository
) : ViewModel() {

    // region 事件流
    private val _uiEvent = MutableSharedFlow<DownloadUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    // endregion

    // region 下载任务状态
    val downloadListState = downloadManager.getAllDownloadTasks()

    private val _allDownloadSegment = downloadTaskRepository.getSegmentAll().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val downloadSortType = appSettingsRepository.appSettingsFlow
        .map { it.downloadSortType }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            AppSettings.DownloadSortType.DownloadSort_TimeDesc
        )

    /**
     * 已完成的下载列表（已排序）
     */
    val completedSegments = combine(
        _allDownloadSegment,
        downloadSortType
    ) { segments, sortType ->
        segments
            .filter { it.downloadState == DownloadState.COMPLETED }
            .sortedWith(getSortComparator(sortType))
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    // endregion

    // region 排序
    fun updateDownloadSortType(sortType: AppSettings.DownloadSortType) {
        viewModelScope.launch {
            appSettingsRepository.updateDownloadSortType(sortType)
        }
    }

    private fun getSortComparator(sortType: AppSettings.DownloadSortType): Comparator<DownloadSegment> {
        return when (sortType) {
            AppSettings.DownloadSortType.DownloadSort_TimeDesc -> compareByDescending { it.updateTime }
            AppSettings.DownloadSortType.DownloadSort_TimeAsc -> compareBy { it.updateTime }
            AppSettings.DownloadSortType.DownloadSort_TitleAsc -> compareBy { it.title }
            AppSettings.DownloadSortType.DownloadSort_TitleDesc -> compareByDescending { it.title }
            AppSettings.DownloadSortType.DownloadSort_SizeDesc -> compareByDescending { it.fileSize }
            AppSettings.DownloadSortType.DownloadSort_SizeAsc -> compareBy { it.fileSize }
            else -> compareByDescending { it.updateTime }
        }
    }
    // endregion

    // region 下载任务控制
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
    // endregion

    // region 文件操作
    /**
     * 请求打开下载的文件
     * 发送事件给 UI 层处理
     */
    fun requestOpenFile(segment: DownloadSegment) {
        val savePath = segment.savePath
        // 检查文件是否存在
        if (!savePath.startsWith("content://")) {
            val file = File(savePath)
            if (!file.exists()) {
                sendToast("文件不存在，可能已被删除")
                return
            }
        }
        viewModelScope.launch {
            _uiEvent.emit(DownloadUiEvent.OpenFile(segment))
        }
    }

    /**
     * 删除多个下载任务及文件
     */
    fun deleteSelectedTasks(segments: List<DownloadSegment>) {
        viewModelScope.launch(Dispatchers.IO) {
            segments.forEach { segment ->
                deleteFileInternal(segment.savePath)
                downloadTaskRepository.deleteSegment(segment.segmentId)
            }
        }
    }

    /**
     * 删除单个下载任务及文件
     */
    fun deleteDownloadSegment(segment: DownloadSegment) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = deleteFileInternal(segment.savePath)
            downloadTaskRepository.deleteSegment(segment.segmentId)

            val message = when (result) {
                DeleteResult.SUCCESS -> "删除成功"
                DeleteResult.FILE_NOT_EXIST -> "文件不存在"
                DeleteResult.FAILED -> "删除失败，文件可能已经被删除或不存在"
            }
            sendToast(message)
        }
    }

    private fun deleteFileInternal(savePath: String): DeleteResult {
        return runCatching {
            if (savePath.startsWith("content://")) {
                val uri = savePath.toUri()
                val rows = contentResolver.delete(uri, null, null)
                if (rows > 0) DeleteResult.SUCCESS else DeleteResult.FAILED
            } else {
                val file = File(savePath)
                when {
                    !file.exists() -> DeleteResult.FILE_NOT_EXIST
                    file.delete() -> DeleteResult.SUCCESS
                    else -> DeleteResult.FAILED
                }
            }
        }.getOrElse { DeleteResult.FAILED }
    }

    private fun sendToast(message: String) {
        viewModelScope.launch {
            _uiEvent.emit(DownloadUiEvent.ShowToast(message))
        }
    }
    // endregion

    private enum class DeleteResult {
        SUCCESS, FILE_NOT_EXIST, FAILED
    }
}