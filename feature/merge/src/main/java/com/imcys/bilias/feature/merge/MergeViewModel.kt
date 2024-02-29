package com.imcys.bilias.feature.merge

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilias.feature.merge.danmaku.IDanmakuParse
import com.imcys.bilias.feature.merge.mix.MixVideoAudio
import com.imcys.common.di.AsDispatchers
import com.imcys.common.di.Dispatcher
import com.imcys.model.download.Entry
import com.imcys.network.download.DownloadManage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MergeViewModel @Inject constructor(
    private val ffmpegMerge: MixVideoAudio,
    private val xmlToAss: IDanmakuParse,
    @Dispatcher(AsDispatchers.IO) private val ioDispatchers: CoroutineDispatcher,
    private val downloadManage: DownloadManage
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _selectedResourceUiState = MutableStateFlow(mutableMapOf<Entry, Boolean>())
    val selectedResourceUiState = _selectedResourceUiState.asStateFlow()

    private val resource = mutableListOf<Entry>()

    init {
        viewModelScope.launch(ioDispatchers) {
            startScanner()
        }
    }

    fun selectResource(e: Entry, selected: Boolean) {
        _selectedResourceUiState.value[e] = !selected
    }

    fun mixVideoAudio(context: Context) {
//        for (entry in resource) {
//            val danmaku = entry.dFile
//
//            val vFile = entry.vFile
//            val aFile = entry.aFile
//            if (vFile == null || aFile == null) return
//
//            val tempFile = File(context.mixDir, entry.title)
//            sendMixData(vFile, aFile, tempFile.absolutePath, entry.title)
//            updatePhotoMedias(context, tempFile)
//        }
    }

    private fun startScanner() {
//        val result = downloadManage.getAllTask(BILI_FULL_PATH)
//        _uiState.update { it.copy(entries = result.toImmutableList()) }
//        for (entry in result) {
//            _selectedResourceUiState.update {
//                it[entry] = false
//                it
//            }
//        }
    }

    private fun parseDanmaku(danmaku: File?, width: Int, height: Int, title: String) {
        danmaku ?: return
        val danmakus = xmlToAss.parse(danmaku)
    }
}

const val BASE_PATH = "/storage/emulated/0/Android/data/"
const val BILI_PATH = "tv.danmaku.bili/download"
const val BILI_FULL_PATH = BASE_PATH + BILI_PATH