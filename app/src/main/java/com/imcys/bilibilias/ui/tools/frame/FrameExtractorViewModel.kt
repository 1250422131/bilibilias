package com.imcys.bilibilias.ui.tools.frame

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFprobeKit
import com.arthenica.ffmpegkit.ReturnCode
import com.imcys.bilibilias.data.repository.DownloadTaskRepository
import com.imcys.bilibilias.database.entity.download.DownloadMode
import com.imcys.bilibilias.database.entity.download.DownloadSegment
import com.imcys.bilibilias.database.entity.download.DownloadState
import com.imcys.bilibilias.download.NewDownloadManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import androidx.documentfile.provider.DocumentFile
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class FrameExtractorViewModel(
    private val downloadManager: NewDownloadManager,
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

    val uiState: StateFlow<UIState>
        field = MutableStateFlow<UIState>(UIState.Default)

    private val _allDownloadSegment = MutableStateFlow<List<DownloadSegment>>(emptyList())
    val allDownloadSegment = _allDownloadSegment.asStateFlow()

    fun importVideo(context: Context, videoPath: String, fps: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val cacheDir = "${context.externalCacheDir?.absolutePath}/frameTemp"
            deleteCacheDir(context)
            File(cacheDir).mkdirs()

            // 复制视频到临时目录（如果是content URI）
            val tempVideoPath = if (videoPath.startsWith("content://")) {
                val tempFile = File(cacheDir, "temp_video.mp4")
                context.contentResolver.openInputStream(videoPath.toUri())?.use { input ->
                    tempFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                tempFile.absolutePath
            } else {
                videoPath
            }

            updateSelectFps(tempVideoPath, fps)
        }
    }

    suspend fun updateSelectFps(videoPath: String? = null, currentFps: Int) =
        withContext(Dispatchers.IO) {
            val tempVideoPath = videoPath ?: when (uiState.value) {
                is UIState.Importing -> (uiState.value as UIState.Importing).selectVideoPath
                is UIState.ImportSuccess -> (uiState.value as UIState.ImportSuccess).videoPath
                else -> return@withContext
            }

            if (tempVideoPath == null) return@withContext

            try {
                // 获取视频帧率
                val videoFps = getVideoFrameRate(tempVideoPath)

                // 提取帧
                val bitmapList = extractFrames(tempVideoPath, currentFps)

                uiState.value = UIState.ImportSuccess(
                    videoFps = videoFps,
                    frameList = bitmapList,
                    selectFps = currentFps,
                    videoPath = tempVideoPath
                )
            } catch (e: Exception) {
                uiState.value = UIState.Default
            }
        }

    private suspend fun getVideoFrameRate(videoPath: String): Int = withContext(Dispatchers.IO) {
        val session = FFprobeKit.getMediaInformation(videoPath)
        if (ReturnCode.isSuccess(session.returnCode)) {
            val mediaInfo = session.mediaInformation
            val videoStream = mediaInfo?.streams?.firstOrNull { it.type == "video" }
            val fpsStr = videoStream?.averageFrameRate
            if (fpsStr != null && fpsStr.contains("/")) {
                val parts = fpsStr.split("/")
                val num = parts[0].toDoubleOrNull() ?: 30.0
                val den = parts[1].toDoubleOrNull() ?: 1.0
                (num / den).toInt()
            } else {
                30
            }
        } else {
            30
        }
    }

    private suspend fun extractFrames(videoPath: String, fps: Int): List<Bitmap> =
        withContext(Dispatchers.IO) {
            val cacheDir = File(videoPath).parent ?: return@withContext emptyList()
            val framesDir = File(cacheDir, "frames").apply { mkdirs() }

            // 清理旧帧
            framesDir.listFiles()?.forEach { it.delete() }

            uiState.value = UIState.Importing(progress = 0f, selectVideoPath = videoPath)

            // FFmpeg命令：提取帧
            val command = "-i \"$videoPath\" -vf fps=$fps \"${framesDir.absolutePath}/frame_%04d.png\""

            val session = FFmpegKit.execute(command)

            if (!ReturnCode.isSuccess(session.returnCode)) {
                Log.e("FrameExtractor", "FFmpeg failed: ${session.failStackTrace}")
                return@withContext emptyList()
            }

            // 加载提取的帧
            val frameFiles = framesDir.listFiles()?.sortedBy { it.name }
            val bitmapList = mutableListOf<Bitmap>()

            frameFiles?.forEachIndexed { index, file ->
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                if (bitmap != null) {
                    bitmapList.add(bitmap)
                }

                val progress = (index + 1).toFloat() / frameFiles.size
                uiState.value = UIState.Importing(
                    progress = progress,
                    selectVideoPath = videoPath
                )
            }

            bitmapList
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
        if (uiState.value is UIState.ImportSuccess) {
            val oldUIState = (uiState.value as UIState.ImportSuccess).copy()
            val uiState = uiState.value as UIState.ImportSuccess
            viewModelScope.launch(Dispatchers.IO) {
                val treeUri = exportUri.toUri()
                val docFile = DocumentFile.fromTreeUri(
                    context, treeUri
                )
                uiState.frameList.forEachIndexed { index, bitmap ->
                    this@FrameExtractorViewModel.uiState.value = UIState.Exporting(
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
                this@FrameExtractorViewModel.uiState.value = oldUIState
            }
        }
    }


}