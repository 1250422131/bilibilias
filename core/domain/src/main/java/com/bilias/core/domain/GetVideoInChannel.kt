package com.bilias.core.domain

import com.imcys.network.repository.user.IUserDataSources
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 获取视频所在的频道列表
 */
class GetVideoInChannel @Inject constructor(private val userRepository: IUserDataSources) {
    operator fun invoke(mId: Long, cId: Long) = flow {
        emit(userRepository.channelList(mId))
    }.flatMapLatest {
        it.list.asFlow()
    }.filterNot {
        it.isLivePlayback
    }.map {
        userRepository.channelVideo(it.mid, it.channelId, 1)
    }.map {
        it.list.archives
    }.map { archives ->
        archives.filter { it.cid == cId }
    }
}



