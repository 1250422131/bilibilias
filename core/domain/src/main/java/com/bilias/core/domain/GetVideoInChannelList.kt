package com.bilias.core.domain

import com.imcys.network.repository.video.IVideoDataSources
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

/**
 * 获取视频所在的频道列表
 */
class GetVideoInChannelList @Inject constructor(private val videoRepository: IVideoDataSources) {
    operator fun invoke(mId: Long, cId: Long) = flow {
        emit(videoRepository.channelList(mId))
    }.flatMapLatest {
        it.list.asFlow()
    }.filterNot {
        it.isLivePlayback
    }.map {
        videoRepository.channelVideo(it.mid, it.channelId, 1)
    }.map {
        it.list.archives
    }.map { archives ->
        archives.filter { it.cid == cId }
    }.map {
        Timber.tag("IVideoDataSources").d(it.toString())
        it
    }
}
