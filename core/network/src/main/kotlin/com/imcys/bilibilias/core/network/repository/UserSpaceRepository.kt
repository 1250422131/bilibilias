package com.imcys.bilibilias.core.network.repository

import com.imcys.bilibilias.core.model.space.FavouredFolder
import com.imcys.bilibilias.core.model.space.SpaceArcSearch
import com.imcys.bilibilias.core.network.api.BILIBILI_URL
import com.imcys.bilibilias.core.network.api.BilibiliApi
import com.imcys.bilibilias.core.network.di.requireWbi
import com.imcys.bilibilias.core.network.utils.parameterMid
import com.imcys.bilibilias.core.network.utils.parameterUpMid
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders
import io.ktor.http.encodeURLParameter
import io.ktor.util.appendIfNameAbsent
import javax.inject.Inject

class UserSpaceRepository @Inject constructor(
    private val client: HttpClient,
) {
    suspend fun 查询用户创建的视频收藏夹(mid: Long): FavouredFolder {
        return client.get(BilibiliApi.FAVOURED_FOLDER_ALL) {
            parameterUpMid(mid)
        }.body()
    }

    suspend fun 查询用户投稿视频(mid: Long, pageNumber: Int): SpaceArcSearch {
        return client.get(BilibiliApi.SPACE_ARC_SEARCH) {
            attributes.put(requireWbi, true)
            header(HttpHeaders.Referrer, "https://space.bilibili.com")
            parameterMid(mid)
            parameter("pn", pageNumber)
            parameter("ps", 30)
//            parameter("tid", 0)
//            parameter("keyword", "")
//            parameter("order", "pubdate")
//            parameter("web_location", "1550101")
//            parameter("platform", "web")
//            parameter("order_avoided", true)
//            parameter("dm_img_list", "[]".encodeURLParameter())
//            parameter("dm_img_str", "V2ViR0wgMS4wIChPcGVuR0wgRVMgMi4wIENocm9taXVtKQ")
//            parameter(
//                "dm_cover_img_str",
//                "QU5HTEUgKE5WSURJQSwgTlZJRElBIEdlRm9yY2UgR1RYIDEwNTAgKDB4MDAwMDFDOEQpIERpcmVjdDNEMTEgdnNfNV8wIHBzXzVfMCwgRDNEMTEpR29vZ2xlIEluYy4gKE5WSURJQS"
//            )
//            parameter(
//                "dm_img_inter",
//                "{\"ds\":[{\"t\":1,\"c\":\"aWNvbmZvbnQgaWNvbi1pY192aWRlbw\",\"p\":[1685,71,575],\"s\":[38,230,-108]}],\"wh\":[4704,7348,58],\"of\":[460,920,460]}".encodeURLParameter()
//            )
        }.body()
    }
}
