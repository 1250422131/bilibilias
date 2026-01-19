package com.imcys.bilibilias.ui.analysis.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.utils.toMenuVideoCode
import com.imcys.bilibilias.data.model.download.DownloadViewInfo
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.model.video.BILIVideoDash
import com.imcys.bilibilias.network.model.video.BILIVideoDurls
import com.imcys.bilibilias.network.model.video.BILIVideoSupportFormat
import com.imcys.bilibilias.network.model.video.convertAudioQualityIdValue
import com.imcys.bilibilias.weight.ASCommonExposedDropdownMenu

/**
 * 音频质量选择
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioQualitySelectScreen(
    modifier: Modifier,
    downloadInfo: DownloadViewInfo?,
    apiStatus: ApiStatus,
    audioList: List<BILIVideoDash.Audio>?,
    onAudioQualityChange: (Long?) -> Unit = {}
) {
    var selectValue: Long by remember { mutableLongStateOf(0) }

    LaunchedEffect(downloadInfo?.selectAudioQualityId) {
        selectValue = downloadInfo?.selectAudioQualityId ?: 0
    }

    if (apiStatus == ApiStatus.ERROR) { return }
    if (!audioList.isNullOrEmpty() || apiStatus != ApiStatus.SUCCESS) {
        ASCommonExposedDropdownMenu(
            modifier = modifier,
            text = convertAudioQualityIdValue(selectValue),
            label = stringResource(R.string.analysis_select_audio_quality),
            values = audioList ?: emptyList(),
            onValue = { convertAudioQualityIdValue(it.id) },
            onSelect = {
                selectValue = it.id
                onAudioQualityChange(it.id)
            }
        )
    }
}

/**
 * 视频质量与编码选择
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoSupportFormatsSelectScreen(
    modifier: Modifier,
    downloadInfo: DownloadViewInfo?,
    mSupportFormats: List<BILIVideoSupportFormat>?,
    dashVideoList: List<BILIVideoDash.Video>?,
    durlVideoList: List<BILIVideoDurls>?,
    onVideoQualityChange: (Long?) -> Unit = {},
    onVideoCodeChange: (String) -> Unit = {}
) {
    // 选择的视频分辨率
    var selectVideoFormatValue: String by remember { mutableStateOf("") }
    // 选择的视频编码
    var selectVideoCodeValue: String by remember { mutableStateOf("") }

    var supportFormats by remember { mutableStateOf(listOf<BILIVideoSupportFormat>()) }
    var videoCodingList by remember { mutableStateOf(setOf<String>()) }

    LaunchedEffect(downloadInfo?.selectVideoCode) {
        selectVideoCodeValue = downloadInfo?.selectVideoCode ?: ""
    }

    LaunchedEffect(downloadInfo?.selectVideoQualityId) {
        supportFormats = if (dashVideoList != null) {
            // Dash模式
            val mVideoCodingList = mutableSetOf<String>()
            mSupportFormats?.filter {
                // 筛选出支持的清晰度
                it.quality == downloadInfo?.selectVideoQualityId
            }?.forEach {
                if (it.codecs.isEmpty()){
                    dashVideoList.forEach { video ->
                        val code = video.codecs.split(".")[0]
                        if (code !in mVideoCodingList){
                            mVideoCodingList.add(code)
                        }
                    }
                } else {
                    it.codecs.forEach { code ->
                        mVideoCodingList.add(code.split(".")[0])
                    }
                }
            }
            videoCodingList = mVideoCodingList
            // 更新视频编码选择，优先选择第一个不是 av01 的编码
            val defaultCode = mVideoCodingList.firstOrNull { it != "av01" } ?: mVideoCodingList.firstOrNull()
            defaultCode?.let {
                onVideoCodeChange(it)
            }
            mSupportFormats?.filter { supportFormat ->
                dashVideoList.any { item -> item.id == supportFormat.quality }
            } ?: emptyList()
        } else {
            // FLV模式
            mSupportFormats?.filter { supportFormat ->
                durlVideoList?.any { item -> item.quality == supportFormat.quality } == true
            }?.ifEmpty {
                // 充电视频下有可能找不到对应的清晰度
                mSupportFormats
            } ?: emptyList()
        }
        selectVideoFormatValue = supportFormats.firstOrNull {
            it.quality == downloadInfo?.selectVideoQualityId
        }?.run {
            description.ifBlank { newDescription }
        } ?: ""
    }



    Row(
        modifier.animateContentSize(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        ASCommonExposedDropdownMenu(
            modifier = Modifier.weight(1f),
            text = selectVideoFormatValue,
            label = stringResource(R.string.analysis_select_resolution),
            values = supportFormats,
            onValue = { it.description.ifBlank { it.newDescription } },
            onSelect = {
                selectVideoFormatValue = it.description.ifBlank { it.newDescription }
                onVideoQualityChange(it.quality)
            }
        )

        if (videoCodingList.isNotEmpty()) {
            ASCommonExposedDropdownMenu(
                modifier = Modifier.weight(1f),
                text = selectVideoCodeValue.toMenuVideoCode(),
                label = stringResource(R.string.analysis_select_codec),
                values = videoCodingList.toList(),
                onValue = { it.toMenuVideoCode() },
                onSelect = {
                    selectVideoCodeValue = it
                    onVideoCodeChange(it)
                }
            )
        }
    }
}
