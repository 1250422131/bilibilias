package com.imcys.bilibilias.ui.tools.frame

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

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

    private val _uiState = MutableStateFlow<UIState>(UIState.Default)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    private val _allDownloadSegment = MutableStateFlow<List<DownloadSegment>>(emptyList())
    val allDownloadSegment = _allDownloadSegment.asStateFlow()

    private var extractionJob: Job? = null
    private var currentVideoPath: String? = null
    private var extractedFramesDir: File? = null

    fun importVideo(context: Context, videoPath: String, fps: Int) {
        extractionJob?.cancel()
        extractionJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                val cacheDir = "${context.externalCacheDir?.absolutePath}/frameTemp"
                deleteCacheDir(context)
                File(cacheDir).mkdirs()

                val tempVideoPath = if (videoPath.startsWith("content://")) {
                    val tempFile = File(cacheDir, "temp_video.mp4")
                    context.contentResolver.openInputStream(videoPath.toUri())?.use { input ->
                        tempFile.outputStream().use { output -> input.copyTo(output) }
                    }
                    tempFile.absolutePath
                } else {
                    videoPath
                }

                currentVideoPath = tempVideoPath
                extractedFramesDir = null
                extractFramesWithFps(tempVideoPath, fps)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("FrameExtractor", "Import failed", e)
                _uiState.value = UIState.Default
            }
        }
    }

    fun updateSelectFps(currentFps: Int) {
        val videoPath = currentVideoPath ?: return

        extractionJob?.cancel()
        extractionJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                extractFramesWithFps(videoPath, currentFps)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("FrameExtractor", "Update fps failed", e)
                _uiState.value = UIState.Default
            }
        }
    }

    private suspend fun extractFramesWithFps(videoPath: String, fps: Int) {
        _uiState.value = UIState.Importing(progress = 0f, selectVideoPath = videoPath)

        val videoFps = getVideoFrameRate(videoPath)
        val framesDir = extractedFramesDir ?: run {
            val cacheDir = File(videoPath).parent ?: return
            File(cacheDir, "frames").apply { mkdirs() }
        }

        val frameFiles = framesDir.listFiles()?.sortedBy { it.name }
        if (frameFiles.isNullOrEmpty()) {
            extractAllFrames(videoPath, framesDir, videoFps) { progress ->
                _uiState.value = UIState.Importing(
                    progress = progress * 0.5f,
                    selectVideoPath = videoPath
                )
            }
            extractedFramesDir = framesDir
        }

        val bitmapList = loadFramesWithFps(framesDir, videoFps, fps) { progress ->
            _uiState.value = UIState.Importing(
                progress = 0.5f + progress * 0.45f,
                selectVideoPath = videoPath
            )
        }

        _uiState.value = UIState.Importing(progress = 1f, selectVideoPath = videoPath)

        _uiState.value = UIState.ImportSuccess(
            videoFps = videoFps,
            frameList = bitmapList,
            selectFps = fps,
            videoPath = videoPath
        )
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

    private suspend fun extractAllFrames(
        videoPath: String,
        framesDir: File,
        videoFps: Int,
        onProgress: (Float) -> Unit
    ) = withContext(Dispatchers.IO) {
        framesDir.listFiles()?.forEach { it.delete() }

        onProgress(0.1f)
        val command = "-i \"$videoPath\" -vf fps=$videoFps \"${framesDir.absolutePath}/frame_%04d.png\""
        val session = FFmpegKit.execute(command)

        if (!ReturnCode.isSuccess(session.returnCode)) {
            Log.e("FrameExtractor", "FFmpeg failed: ${session.failStackTrace}")
            throw Exception("FFmpeg extraction failed")
        }
        onProgress(1f)
    }

    private suspend fun loadFramesWithFps(
        framesDir: File,
        videoFps: Int,
        selectFps: Int,
        onProgress: (Float) -> Unit
    ): List<Bitmap> = withContext(Dispatchers.IO) {
        val allFrameFiles = framesDir.listFiles()?.sortedBy { it.name } ?: return@withContext emptyList()
        if (allFrameFiles.isEmpty()) return@withContext emptyList()

        val interval = (videoFps.toFloat() / selectFps).coerceAtLeast(1f)
        val selectedFrames = mutableListOf<File>()
        var currentIndex = 0f
        while (currentIndex.toInt() < allFrameFiles.size) {
            selectedFrames.add(allFrameFiles[currentIndex.toInt()])
            currentIndex += interval
        }

        val bitmapList = mutableListOf<Bitmap>()
        selectedFrames.forEachIndexed { index, file ->
            BitmapFactory.decodeFile(file.absolutePath)?.let { bitmapList += it }
            onProgress((index + 1).toFloat() / selectedFrames.size)
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
                            null
                        } else if (savePath.startsWith("content://")) {
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
        val cacheDir = "${context.externalCacheDir?.absolutePath}/frameTemp"
        if (File(cacheDir).exists()) {
            File(cacheDir).deleteRecursively()
        }
        currentVideoPath = null
        extractedFramesDir = null
    }

    fun exportFrameToImage(context: Context, exportUri: String) {
        val currentState = _uiState.value
        if (currentState !is UIState.ImportSuccess) return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val treeUri = exportUri.toUri()
                val docFile = DocumentFile.fromTreeUri(context, treeUri) ?: return@launch

                currentState.frameList.forEachIndexed { index, bitmap ->
                    _uiState.value = UIState.Exporting(
                        progress = (index + 1) / currentState.frameList.size.toFloat(),
                        exportPath = exportUri
                    )
                    val fileName = "frame_${index + 1}.png"
                    val file = docFile.createFile("image/png", fileName)
                    file?.uri?.let { fileUri ->
                        contentResolver.openOutputStream(fileUri)?.use { out ->
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                        }
                    }
                }
                _uiState.value = currentState
            } catch (e: Exception) {
                Log.e("FrameExtractor", "Export failed", e)
                _uiState.value = currentState
            }
        }
    }
}
