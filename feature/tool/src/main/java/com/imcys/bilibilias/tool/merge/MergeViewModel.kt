package com.imcys.bilibilias.tool.merge

import android.content.Context
import android.media.MediaFormat
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.contentValuesOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.tool.danmaku.ASSBuild
import com.imcys.bilibilias.tool.danmaku.IDanmakuParse
import com.imcys.common.di.AsDispatchers
import com.imcys.common.di.Dispatcher
import com.imcys.common.utils.FileUtils
import com.imcys.common.utils.updatePhotoMedias
import com.imcys.model.download.Entry
import com.imcys.network.download.AUDIO_M4S
import com.imcys.network.download.DANMAKU_XML
import com.imcys.network.download.ENTRY_JSON
import com.imcys.network.download.FFmpegMerge
import com.imcys.network.download.MergeData
import com.imcys.network.download.VIDEO_M4S
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MergeViewModel @Inject constructor(
    private val fFmpegMerge: FFmpegMerge,
    private val json: Json,
    private val xmlToAss: IDanmakuParse,
    private val assBuild: ASSBuild,
    @ApplicationContext private val context: Context,
    @Dispatcher(AsDispatchers.IO) private val ioDispatchers: CoroutineDispatcher
) : ViewModel() {
    private val _scanResultUiState = MutableStateFlow<ScanResultUiState>(ScanResultUiState.Empty)
    val scanResultUiState = _scanResultUiState.asStateFlow()
    private val _mergeUiState = MutableStateFlow<MergeUiState>(MergeUiState.Error(null))
    val mixUiState = _scanResultUiState.asStateFlow()

    private val saveData = mutableMapOf<Entry, MutableList<File>>()
    private val listener = object : FFmpegMerge.Listener {
        override fun onProgress(progress: Int, pts: Long) {
            _mergeUiState.update { MergeUiState.Merging(progress) }
        }

        override fun onError(errorCode: Int, errorMsg: String?, mixFile: String, realName: String) {
            _mergeUiState.update { MergeUiState.Error(errorMsg) }
        }

        override fun onComplete() {
            _mergeUiState.update { MergeUiState.Complete }
        }

        override fun onStart(title: String) {
            _mergeUiState.update { MergeUiState.Start(title) }
        }

        override fun onSuccess(fullPath: String, realName: String, tasks: Int) {
            _mergeUiState.update {
                MergeUiState.Success(realName)
            }
            Timber.d(fullPath)
            moveToMediaStore(fullPath, realName)
        }
    }

    init {
        fFmpegMerge.setListener(listener)
    }

    private fun moveToMediaStore(fullPath: String, realName: String) {
        viewModelScope.launch(ioDispatchers) {
            getUri(realName)?.let { uri ->
                try {
                    val sink = context.contentResolver.openOutputStream(uri)
                    if (sink != null) {
                        val source = File("$fullPath.mp4").inputStream()
                        FileUtils.copy(source, sink)
                        Timber.d("复制完成$fullPath")
                    }
                } catch (e: Exception) {
                    Timber.d(e)
                }
            }
        }
    }

    fun mixVideoAudio(entries: List<Entry>) {
        for (entry in entries) {
            val list = saveData[entry] ?: continue
            val danmaku = list.find { it.name == DANMAKU_XML }

            val vFile = list.find { it.name == VIDEO_M4S }
            val aFile = list.find { it.name == AUDIO_M4S }
            if (vFile == null || aFile == null) return

            val tempFile = File(context.filesDir, entry.title)
            sendMixData(vFile, aFile, tempFile.absolutePath, entry.title)
            updatePhotoMedias(context, tempFile)
        }
    }

    private fun sendMixData(vFile: File, aFile: File, file: String, title: String) {
        fFmpegMerge.execute(MergeData(vFile, aFile, file, title))
    }

    fun scanFile() {
        val scannedFiles = ArrayDeque<MutableList<File>>(32)
        File(BILI_FULL_PATH)
            .walkTopDown()
            .forEach { file ->
                if (file.name == ENTRY_JSON) {
                    scannedFiles.addLast(mutableListOf(file))
                }
                if (file.name == VIDEO_M4S || file.name == AUDIO_M4S || file.name == DANMAKU_XML) {
                    scannedFiles.last {
                        it.add(file)
                    }
                }
            }
        // 解析文件
        parsingFile(scannedFiles)
    }

    private fun parseDanmaku(danmaku: File?, width: Int, height: Int, title: String) {
        danmaku ?: return
        val danmakus = xmlToAss.danmakuFrom(danmaku)
        val s = assBuild.loadDanmaku(danmakus, title, width.toString(), height.toString())
    }

    private fun getUri(title: String): Uri? {
        val values = contentValuesOf(
            MediaStore.Video.Media.MIME_TYPE to MediaFormat.MIMETYPE_VIDEO_MPEG4,
            MediaStore.Video.Media.DISPLAY_NAME to "$title.mp4",
            MediaStore.Video.Media.RELATIVE_PATH to "${Environment.DIRECTORY_MOVIES}/biliAS",
        )
        val contentResolver = context.contentResolver
        return contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun parsingFile(scannedFiles: ArrayDeque<MutableList<File>>) {
        saveData.clear()
        _scanResultUiState.update { ScanResultUiState.Loading }
        val parseResult = scannedFiles.filter { it.size <= 4 }.associateBy {
            val entryFile = it.removeFirst()
            val entry = json.decodeFromStream<Entry>(entryFile.inputStream())
            entry
        }
        if (parseResult.isEmpty()) {
            _scanResultUiState.update {
                ScanResultUiState.Empty
            }
        } else {
            _scanResultUiState.update {
                ScanResultUiState.Success(parseResult.keys.toImmutableList())
            }
        }
        saveData.putAll(parseResult)
    }
}
