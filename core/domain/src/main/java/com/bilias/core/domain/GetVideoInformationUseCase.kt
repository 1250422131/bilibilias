package com.bilias.core.domain

import com.imcys.model.video.ViewDetailAndPlayUrl
import com.imcys.network.repository.video.IVideoDataSources
import javax.inject.Inject

class GetVideoDetailAndPlayUrlUseCase @Inject constructor(
    private val videoRepository: IVideoDataSources,
) {
    suspend operator fun invoke(bvid: String): ViewDetailAndPlayUrl {
        val details = videoRepository.getDetail(bvid)
        val playerPlayUrl = videoRepository.getPlayerPlayUrl(bvid, details.cid)

        return ViewDetailAndPlayUrl(
            details.aid,
            details.bvid,
            details.cid,
            details.title,
            playerPlayUrl.dash,
            details,
            playerPlayUrl
        )
    }
}