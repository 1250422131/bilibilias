package com.imcys.bilibilias.ui.tools.frame

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.data.repository.DownloadTaskRepository
import com.imcys.bilibilias.database.entity.download.DownloadMode
import com.imcys.bilibilias.database.entity.download.DownloadSegment
import com.imcys.bilibilias.database.entity.download.DownloadState
import com.imcys.bilibilias.dwonload.DownloadManager
import com.imcys.bilibilias.ffmpeg.FFmpegManger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class FrameExtractorViewModel(
    private val downloadManager: DownloadManager,
    private val downloadTaskRepository: DownloadTaskRepository,
    private val contentResolver: ContentResolver
) : ViewModel() {

    data class UIState(
        val selectVideoPath: String? = null,
    )

    private val _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()
    private val _allDownloadSegment = MutableStateFlow<List<DownloadSegment>>(emptyList())
    val allDownloadSegment = _allDownloadSegment.asStateFlow()

    private val _fpsList = MutableStateFlow<List<String>>(emptyList())

    val fpsList = _fpsList.asStateFlow()


    fun initVideoInfo(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            downloadTaskRepository.getSegmentAll().collect {
                _allDownloadSegment.emit(it.filter { segment ->
                    segment.downloadState == DownloadState.COMPLETED &&
                            (segment.downloadMode == DownloadMode.VIDEO_ONLY || segment.downloadMode == DownloadMode.AUDIO_VIDEO)
                })

//
//                it.firstOrNull()?.let { segment ->
//                    // 获取Android/data/com.imcys.bilibilias/files目录
//                    val cacheDir = "${context.externalCacheDir?.absolutePath}/frames/${segment.platformId}"
//                    // 获取视频的所有帧
//                    val result = FFmpegManger.getVideoFramesCompat(
//                        context,
//                        segment.savePath.toUri(),
//                        cacheDir
//                    )
//                    _fpsList.emit(result.first)
//                }
            }
        }
    }

    // 获取图片文件bitmap
    fun getFrameBitmap(context: Context, imagePath: String): Bitmap? {
        return try {
            val uri = if (imagePath.startsWith("content://")) {
                imagePath.toUri()
            } else {
                val file = File(imagePath)
                if (!file.exists()) return null
                file.toUri()
            }
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


}