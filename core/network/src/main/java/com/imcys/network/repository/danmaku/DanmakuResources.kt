package com.imcys.network.repository.danmaku

import com.imcys.network.repository.Parameter
import com.imcys.network.repository.WebInterface

//@Resource("x/v1/dm/list.so")
class DanmakuXml(val cid: Long)

/**
 * https://api.bilibili.com/x/v2/dm/wbi/web/seg.so?
 * type=1&
 * oid=1283745701&
 * pid=619092919&
 * segment_index=1&
 * pull_mode=1&
 * ps=0&
 * pe=120000&
 * web_location=1315873&
 * w_rid=03838b4362dc45b1ab4784e6cd1a6f18&
 * wts=1697211774
 *
 * https://api.bilibili.com/x/v2/dm/web/seg.so?type=1&oid=1323155344&pid=705616324&segment_index=1
 * https://www.bilibili.com/video/BV14Q4y1n7jo/
 * 视频时长 30:04
 * segment_index 长度为 6
 * (30*60 + 4) / (6*60) + 1
 */
//@Resource("x/v2/dm")
class DanmakuProto {
//    @Resource("web/seg.so")
    class Web(val cid: Long, val index: Int, val type: Int = 1)

//    @Resource("wbi/web/seg.so")
    class WbiWeb(val cid: Long, val index: Int, val type: Int = 1) : WebInterface {
        override fun buildParameter(): List<Parameter> {
            return listOf(
                Parameter("oid", cid.toString()),
                Parameter("type", type.toString()),
                Parameter("segment_index", index.toString())
            )
        }
    }
}
