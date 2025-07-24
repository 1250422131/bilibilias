package com.imcys.bilibilias.data.model.download

import com.imcys.bilibilias.data.model.video.ASLinkResultType
import com.imcys.bilibilias.database.entity.download.DownloadMode
import java.util.TreeSet

data class DownloadViewInfo(
    val selectVideoQualityId: Long? = null,
    val selectVideoCode: String = "",
    val selectAudioQualityId: Long? = null,
    val downloadMode: DownloadMode = DownloadMode.AUDIO_VIDEO,
    val selectedCid: List<Long> = listOf(),
    val selectedEpId: List<Long> = listOf(),
) {
    fun toggleCid(cid: Long): DownloadViewInfo = copy(
        selectedCid = if (selectedCid.contains(cid)) {
            selectedCid - cid
        } else {
            selectedCid + cid
        }
    )

    fun toggleEpId(epId: Long): DownloadViewInfo = copy(
        selectedEpId = if (selectedEpId.contains(epId)) {
            selectedEpId - epId
        } else {
            selectedEpId + epId
        }
    )

    fun updateVideoQuality(qualityId: Long?, code: String): DownloadViewInfo = copy(
        selectVideoQualityId = qualityId,
        selectVideoCode = code
    )

    fun updateAudioQuality(qualityId: Long?): DownloadViewInfo = copy(
        selectAudioQualityId = qualityId
    )

    /**
     * 根据媒体类型智能更新配置，确保只保留相关选中项
     */
    fun updateForMediaType(
        mediaType: ASLinkResultType,
        qualityId: Long?,
        code: String,
        audioQualityId: Long?,
        defaultCid: Long? = null,
        defaultEpId: Long? = null
    ): DownloadViewInfo = when (mediaType) {
        is ASLinkResultType.BILI.Video -> copy(
            selectVideoQualityId = qualityId,
            selectVideoCode = code,
            selectAudioQualityId = audioQualityId,
            selectedCid = if (defaultCid != null) selectedCid + defaultCid else selectedCid,
            selectedEpId = emptyList() // 清空番剧选择
        )

        is ASLinkResultType.BILI.Donghua -> copy(
            selectVideoQualityId = qualityId,
            selectVideoCode = code,
            selectAudioQualityId = audioQualityId,
            selectedEpId = if (defaultEpId != null) selectedEpId + defaultEpId else selectedEpId,
            selectedCid = emptyList() // 清空视频选择
        )

        is ASLinkResultType.BILI.User -> this // 用户页面不需要下载配置
    }
}