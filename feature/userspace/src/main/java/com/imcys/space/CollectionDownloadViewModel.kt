package com.imcys.space

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bilias.core.domain.GetVideoDetailAndPlayUrlUseCase
import com.imcys.network.download.DownloadManage
import com.imcys.space.navigation.itemsBV
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionDownloadViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val downloadManage: DownloadManage,
    getVideoDetailAndPlayUrlUseCase: GetVideoDetailAndPlayUrlUseCase
) : ViewModel() {
    private val items: String = checkNotNull(savedStateHandle[itemsBV])

    private val _collectionState = MutableStateFlow(ItemState())
    val collectionState = _collectionState.asStateFlow()
    private val videoQuality = mutableMapOf<String, Int>()

    /** 1.请求基本信息 2.请求播放链接 3.下载弹幕 4.写入entry.json */
    init {
        viewModelScope.launch {
            for (item in items.split(",")) {
                val viewDetailAndPlayUrl = getVideoDetailAndPlayUrlUseCase(item)
                _collectionState.update {
                    val viewDetailAndPlayUrls =
                        it.items.toPersistentList().add(viewDetailAndPlayUrl)
                    it.copy(items = viewDetailAndPlayUrls)
                }
            }
        }
    }

    fun changeQuality(bvid: String, quality: Int) {
        videoQuality[bvid] = quality
    }

    fun startDownload() {
        videoQuality.entries.forEach { (bvid, quality) ->
            downloadManage.addTask(bvid,quality)
        }
    }
}
