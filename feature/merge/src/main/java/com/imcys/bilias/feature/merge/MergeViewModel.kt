package com.imcys.bilias.feature.merge

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilias.feature.merge.danmaku.IDanmakuParse
import com.imcys.bilias.feature.merge.mix.FFmpegMerge
import com.imcys.bilias.feature.merge.mix.MergeData
import com.imcys.bilias.feature.merge.move.mixDir
import com.imcys.common.di.AsDispatchers
import com.imcys.common.di.Dispatcher
import com.imcys.common.utils.updatePhotoMedias
import com.imcys.model.download.Entry
import com.imcys.network.download.DownloadManage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MergeViewModel @Inject constructor(
    private val ffmpegMerge: FFmpegMerge,
    private val xmlToAss: IDanmakuParse,
    @Dispatcher(AsDispatchers.IO) private val ioDispatchers: CoroutineDispatcher,
    private val downloadManage: DownloadManage
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _selectedResourceUiState = MutableStateFlow(mutableMapOf<Entry, Boolean>())
    val selectedResourceUiState = _selectedResourceUiState.asStateFlow()

    private val resource = mutableListOf<Entry>()

    private val listener = object : FFmpegMerge.Listener {
        override fun onProgress(progress: Int, pts: Long) {
            _uiState.update {
                it.copy(progress = progress)
            }
        }

        override fun onError(errorCode: Int, errorMsg: String?, mixFile: String, realName: String) {
            _uiState.update {
                it.copy(errorMessage = "$realName 合并错误")
            }
        }

        override fun onComplete() {
            _uiState.update {
                it.copy(complete = true, startMix = false)
            }
        }

        override fun onStart(title: String) {
            _uiState.update {
                it.copy(current = "当前任务 $title", complete = false, startMix = true)
            }
        }

        override fun onSuccess(fullPath: String, title: String, tasks: Int) {
            _uiState.update {
                it.copy(mixMessage = "合并成功 $title, 剩余 $tasks 任务")
            }
        }
    }
    val newUiState: StateFlow<NewUiState> =
        downloadManage.getAllTaskFlow(BILI_FULL_PATH)
            .map {
                if (it.isEmpty()) {
                    NewUiState.Empty
                } else {
                    NewUiState.Success(it.toImmutableList())
                }
            }.stateIn(viewModelScope, SharingStarted.Eagerly, NewUiState.Loading)

    init {
        viewModelScope.launch(ioDispatchers) {
            startScanner()
        }
        ffmpegMerge.setListener(listener)
    }

    fun selectResource(e: Entry, selected: Boolean) {
        _selectedResourceUiState.value[e] = !selected
        newUiState.value
    }

    fun mixVideoAudio(context: Context) {
        for (entry in resource) {
            val danmaku = entry.dFile

            val vFile = entry.vFile
            val aFile = entry.aFile
            if (vFile == null || aFile == null) return

            val tempFile = File(context.mixDir, entry.title)
            sendMixData(vFile, aFile, tempFile.absolutePath, entry.title)
            updatePhotoMedias(context, tempFile)
        }
    }

    private fun sendMixData(vFile: File, aFile: File, file: String, title: String) {
        ffmpegMerge.execute(MergeData(vFile, aFile, file, title))
    }

    private fun startScanner() {
        val result = downloadManage.getAllTask(BILI_FULL_PATH)
        _uiState.update { it.copy(entries = result.toImmutableList()) }
        for (entry in result) {
            _selectedResourceUiState.update {
                it[entry] = false
                it
            }
        }
    }

    private fun parseDanmaku(danmaku: File?, width: Int, height: Int, title: String) {
        danmaku ?: return
        val danmakus = xmlToAss.parse(danmaku)
    }
}

const val BASE_PATH = "/storage/emulated/0/Android/data/"
const val BILI_PATH = "tv.danmaku.bili/download"
const val BILI_FULL_PATH = BASE_PATH + BILI_PATH