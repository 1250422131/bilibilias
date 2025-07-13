package com.imcys.bilibilias.data.model.video

import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.model.user.BILIUserSpaceAccInfo
import com.imcys.bilibilias.network.model.video.BILIDonghuaSeasonInfo
import com.imcys.bilibilias.network.model.video.BILIVideoViewInfo

sealed interface ASLinkResultType {
    sealed interface BILI : ASLinkResultType {
        data class Video(
            val currentBvId: String,
            val viewInfo: NetWorkResult<BILIVideoViewInfo?>
        ) : BILI

        // Anime
        data class Donghua(
            val currentEpId: Long,
            val donghuaViewInfo: NetWorkResult<BILIDonghuaSeasonInfo?>
        ) : BILI

        data class User(
            val userInfo: NetWorkResult<BILIUserSpaceAccInfo?>
        ): BILI
    }
}