package com.imcys.bilibilias.ui.tools.frame

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.data.repository.DownloadTaskRepository
import com.imcys.bilibilias.database.entity.download.DownloadMode
import com.imcys.bilibilias.database.entity.download.DownloadSegment
import com.imcys.bilibilias.database.entity.download.DownloadState
import com.imcys.bilibilias.download.DownloadManager
import com.imcys.bilibilias.ffmpeg.FFmpegManger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import androidx.core.graphics.createBitmap
import androidx.documentfile.provider.DocumentFile
import kotlinx.coroutines.withContext
import java.nio.ByteBuffer

class FrameExtractorViewModel(
    private val downloadManager: DownloadManager,
    private val downloadTaskRepository: DownloadTaskRepository,
    private val contentResolver: ContentResolver
) : ViewModel() {

    sealed interface UIState {
        data object Default : UIState

        data class Importing(
            val progress: Float = 0f,
            val selectVideoPath: String? = null,
        ) : UIState

        data class ImportSuccess(
            val videoPath: String? = null,
            val frameList: List<Bitmap> = emptyList(),
            val videoDuration: Int = 0,
            val videoFps: Int,
            val selectFps: Int = 1
        ) : UIState

        data class Exporting(
            val progress: Float = 0f,
            val exportPath: String = "",
        ) : UIState

    }

    private val _uiState = MutableStateFlow<UIState>(UIState.Default)
    val uiState = _uiState.asStateFlow()
    private val _allDownloadSegment = MutableStateFlow<List<DownloadSegment>>(emptyList())
    val allDownloadSegment = _allDownloadSegment.asStateFlow()

    fun importVideo(context: Context, videoPath: String, fps: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val cacheDir =
                "${context.externalCacheDir?.absolutePath}/frameTemp"
            deleteCacheDir(context)
            // 获取临时存储路径
            val tempVideoPath = FFmpegManger.getVideoTempPath(
                context,
                videoPath.toUri(),
                cacheDir
            )
            updateSelectFps(tempVideoPath, fps)
        }
    }

    suspend fun updateSelectFps(videoPath: String? = null, currentFps: Int) =
        withContext(Dispatchers.IO) {

            val tempVideoPath = videoPath ?: when (_uiState.value) {
                is UIState.Importing -> {
                    (_uiState.value as UIState.Importing).selectVideoPath
                }

                is UIState.ImportSuccess -> {
                    (_uiState.value as UIState.ImportSuccess).videoPath
                }

                else -> {
                    return@withContext
                }
            }

            val videoFps = FFmpegManger.getVideoFrameRate(tempVideoPath ?: return@withContext)
            val bitmapList = mutableListOf<Bitmap>()

            runCatching {
                FFmpegManger.getVideoFramesJNI(
                    tempVideoPath,
                    currentFps,
                    object : FFmpegManger.FFmpegFrameListener {
                        override fun onFrame(
                            data: ByteArray,
                            width: Int,
                            height: Int,
                            index: Int
                        ) {
                            val bitmap = createBitmap(width, height)
                            val buffer = ByteBuffer.wrap(data)
                            val pixels = IntArray(width * height)
                            var i = 0
                            while (buffer.remaining() >= 3 && i < pixels.size) {
                                val r = buffer.get().toInt() and 0xFF
                                val g = buffer.get().toInt() and 0xFF
                                val b = buffer.get().toInt() and 0xFF
                                pixels[i] = (0xFF shl 24) or (r shl 16) or (g shl 8) or b
                                i++
                            }
                            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
                            bitmapList.add(bitmap)
                        }

                        override fun onProgress(progress: Int) {
                            _uiState.value = UIState.Importing(
                                progress = progress / 100f,
                                selectVideoPath = tempVideoPath
                            )
                        }

                        override fun onComplete() {
                            _uiState.value =
                                UIState.ImportSuccess(
                                    videoFps = videoFps,
                                    frameList = bitmapList,
                                    selectFps = currentFps,
                                    videoPath = tempVideoPath
                                )
                        }

                    })
            }
        }


    fun initVideoInfo(context: Context) {
        deleteCacheDir(context)
        viewModelScope.launch(Dispatchers.IO) {
            downloadTaskRepository.getSegmentAll().collect { list ->
                _allDownloadSegment.emit(
                    list.filter { segment ->
                        segment.downloadState == DownloadState.COMPLETED &&
                                (segment.downloadMode == DownloadMode.VIDEO_ONLY ||
                                        segment.downloadMode == DownloadMode.AUDIO_VIDEO)
                    }.mapNotNull { segment ->
                        val savePath = segment.savePath
                        if (savePath.isBlank()) {
                            // 没有保存路径，忽略
                            null
                        } else if (savePath.startsWith("content://")) {
                            // Android 10+：MediaStore Uri
                            val fileUri = savePath.toUri()
                            val docFile = DocumentFile.fromSingleUri(context, fileUri)
                            if (docFile == null || !docFile.exists()) {
                                null
                            } else {
                                val retriever = MediaMetadataRetriever()
                                try {
                                    retriever.setDataSource(context, fileUri)
                                    val durationStr = retriever.extractMetadata(
                                        MediaMetadataRetriever.METADATA_KEY_DURATION
                                    )
                                    val durationMs = durationStr?.toLongOrNull() ?: 0L
                                    segment.apply { tempDuration = durationMs }
                                } catch (_: Exception) {
                                    null
                                } finally {
                                    retriever.release()
                                }
                            }
                        } else {
                            // Android 10 以下：绝对路径文件
                            val file = File(savePath)
                            if (!file.exists()) {
                                null
                            } else {
                                val retriever = MediaMetadataRetriever()
                                try {
                                    retriever.setDataSource(file.absolutePath)
                                    val durationStr = retriever.extractMetadata(
                                        MediaMetadataRetriever.METADATA_KEY_DURATION
                                    )
                                    val durationMs = durationStr?.toLongOrNull() ?: 0L
                                    segment.apply { tempDuration = durationMs }
                                } catch (_: Exception) {
                                    null
                                } finally {
                                    retriever.release()
                                }
                            }
                        }
                    }
                )
            }
        }
    }

    fun deleteCacheDir(context: Context) {
        val cacheDir =
            "${context.externalCacheDir?.absolutePath}/frameTemp"
        if (File(cacheDir).exists()) {
            // 删除旧文件
            File(cacheDir).deleteRecursively()
        }
    }


    fun exportFrameToImage(context: Context, exportUri: String) {
        if (_uiState.value is UIState.ImportSuccess) {
            val oldUIState = (_uiState.value as UIState.ImportSuccess).copy()
            val uiState = _uiState.value as UIState.ImportSuccess
            viewModelScope.launch(Dispatchers.IO) {
                val treeUri = exportUri.toUri()
                val docFile = DocumentFile.fromTreeUri(
                    context, treeUri
                )
                uiState.frameList.forEachIndexed { index, bitmap ->
                    _uiState.value = UIState.Exporting(
                        progress = (index + 1) / uiState.frameList.size.toFloat(),
                        exportPath = exportUri
                    )
                    val fileName = "frame_${index + 1}.png"
                    val file = docFile?.createFile("image/png", fileName)
                    file?.uri?.let { fileUri ->
                        contentResolver.openOutputStream(fileUri)?.use { out ->
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                        }
                    }
                }
                _uiState.value = oldUIState
            }
        }
    }


}