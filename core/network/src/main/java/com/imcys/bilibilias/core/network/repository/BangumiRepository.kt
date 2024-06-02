package com.imcys.bilibilias.core.network.repository

import com.imcys.bilibilias.core.model.bangumi.BangumiDetail
import com.imcys.bilibilias.core.model.bangumi.BangumiStreamUrl
import com.imcys.bilibilias.core.network.utils.parameterAVid
import com.imcys.bilibilias.core.network.utils.parameterCid
import com.imcys.bilibilias.core.network.utils.parameterEPid
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class BangumiRepository @Inject constructor(
    private val client: HttpClient,
) {
    suspend fun 获取剧集详情(epid: Long): BangumiDetail {
        return client.get("pgc/view/web/season") {
            parameterEPid(epid)
        }.body()
    }

    suspend fun videoStreamingURL(aid: Long, cid: Long, epid: Long): BangumiStreamUrl {
        return client.get("/pgc/player/web/v2/playurl") {
            parameter("support_multi_audio", true)
            parameterAVid(aid)
            parameterCid(cid)
            parameterEPid(epid)
            parameter("qn", 127)
            parameter("fnver", 0)
            parameter("fnval", 4048)
            parameter("fourk", 1)
            parameter("from_client", "BROWSER")
            parameter("is_main_page", true)
            parameter("need_fragment", true)
            parameter("voice_balance", 1)
            parameter("drm_tech_type", 2)
        }.body()
    }
}
