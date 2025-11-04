package com.imcys.bilibilias.data.model.download

import com.imcys.bilibilias.data.model.video.ASLinkResultType
import com.imcys.bilibilias.database.entity.download.DownloadMode
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.emptyNetWorkResult
import com.imcys.bilibilias.network.model.user.BILIUserSpaceAccInfo
import com.imcys.bilibilias.network.model.video.BILIVideoLanguage
import com.imcys.bilibilias.network.model.video.BILIVideoLanguageItem
import com.imcys.bilibilias.network.model.video.BILIVideoPlayerInfoV2
import java.util.TreeSet


enum class CCFileType {
    ASS,
    SRT,
}
data class DownloadViewInfo(
    val selectVideoQualityId: Long? = null,
    val selectVideoCode: String = "",
    val selectAudioQualityId: Long? = null,
    val downloadMode: DownloadMode = DownloadMode.AUDIO_VIDEO,
    val selectedCid: List<Long> = listOf(),
    val selectedEpId: List<Long> = listOf(),
    val downloadMedia : Boolean = true,
    val downloadCover: Boolean = false,
    val downloadDanmaku: Boolean = false,
    val selectAudioLanguage: BILIVideoLanguageItem? = null,
    val selectedCCId: List<Long> = listOf(), // 字幕 ID 列表
    val ccFileType: CCFileType = CCFileType.SRT, // 字幕文件类型
    val videoPlayerInfoV2: NetWorkResult<BILIVideoPlayerInfoV2?> = emptyNetWorkResult()
) {


    // 清空cid列表
    fun clearCidList(): DownloadViewInfo = copy(
        selectedCid = emptyList()
    )

    // 清空epId列表
    fun clearEpIdList(): DownloadViewInfo = copy(
        selectedEpId = emptyList()
    )

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


    fun clearCCIdList(): DownloadViewInfo = copy(
        selectedCCId = emptyList()
    )

    fun toggleCCId(ccId: Long): DownloadViewInfo = copy(
        selectedCCId = if (selectedCCId.contains(ccId)) {
            selectedCCId - ccId
        } else {
            // 使用 TreeSet 去重并排序
            (TreeSet(selectedCCId) + ccId).toList()
        }
    )



    fun updateCCFileType(fileType: CCFileType): DownloadViewInfo = copy(
        ccFileType = fileType
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
            // 防止重复添加和 0L 添加
            selectedCid = (if (defaultCid != null && !selectedCid.contains(defaultCid)) selectedCid + defaultCid else selectedCid).filter { it != 0L },
            selectedEpId = emptyList() // 清空番剧选择
        )

        is ASLinkResultType.BILI.Donghua -> copy(
            selectVideoQualityId = qualityId,
            selectVideoCode = code,
            selectAudioQualityId = audioQualityId,
            // 防止重复添加和 0L 添加
            selectedEpId = (if (defaultEpId != null && !selectedEpId.contains(defaultEpId)) selectedEpId + defaultEpId else selectedEpId).filter { it != 0L },
            selectedCid = emptyList() // 清空视频选择
        )

        is ASLinkResultType.BILI.User -> this // 用户页面不需要下载配置
    }
}