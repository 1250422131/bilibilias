package com.imcys.network.repository.space

import com.imcys.common.di.AsDispatchers
import com.imcys.common.di.Dispatcher
import com.imcys.model.SpaceChannelVideo
import com.imcys.model.space.SpaceArcSearch
import com.imcys.network.repository.WbiKeyRepository
import com.imcys.network.utils.parameterList
import com.imcys.network.utils.parameterMID
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

private const val SPACE_WBI_ARC_SEARCH = "x/space/wbi/arc/search"

private const val SPACE_CHANNEL_VIDEO = "x/space/channel/video"

/**
 * ![用户空间相关](https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/user/space.md)
 */
@Singleton
class SpaceRepository @Inject constructor(
    private val httpClient: HttpClient,
    private val wbiKeyRepository: WbiKeyRepository,
    @Dispatcher(AsDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) {
    /**
     * | 参数名  | 类型 | 内容         | 必要性 | 备注                                                                          |
     * | ------- | ---- | ------------ | ------ | ----------------------------------------------------------------------------- |
     * | mid     | num  | 目标用户mid  | 必要   |                                                                               |
     * | order   | str  | 排序方式     | 非必要 | 默认为pubdate<br />最新发布：pubdate<br />最多播放：click<br />最多收藏：stow |
     * | tid     | num  | 筛选目标分区 | 非必要 | 默认为0<br />0：不进行分区筛选<br />分区tid为所筛选的分区                     |
     * | keyword | str  | 关键词筛选   | 非必要 | 用于使用关键词搜索该UP主视频稿件                                              |
     * | pn      | num  | 页码         | 非必要 | 默认为 `1`                                                                    |
     * | ps      | num  | 每页项数     | 非必要 | 默认为 `30`                                                                   |
     *
     */
    suspend fun querySpaceArc(mid: Long, pn: Int): SpaceArcSearch = withContext(ioDispatcher) {
        val token = wbiKeyRepository.getUserNavToken(
            emptyList()
//            listOf(
//                "mid" to mid,
//                "pn" to pn,
//                "ps" to PS,
//
//                "tid" to 0,
//                "order" to "pubdate"
//            )
        )
        httpClient.get(SPACE_WBI_ARC_SEARCH) {
            parameterList(token)
        }.body()
    }

    /**
     * 查询用户频道中的视频
     * curl -G 'https://api.bilibili.com/x/space/channel/video' \
     * --data-urlencode 'mid=53456' \
     * --data-urlencode 'cid=170' \
     */
    suspend fun queryUserChannelsVideos(mid: Long, cid: Long): SpaceChannelVideo = withContext(ioDispatcher) {
        httpClient.get(SPACE_CHANNEL_VIDEO) {
            parameterMID(mid)
            parameter("cid", cid)
        }.body()
    }
}