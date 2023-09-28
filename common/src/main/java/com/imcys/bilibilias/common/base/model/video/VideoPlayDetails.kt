package com.imcys.bilibilias.common.base.model.video

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 视频下载信息数据类
 *
 * ![获取视频流地址_web端](https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/video/videostream_url.md#%E8%8E%B7%E5%8F%96%E8%A7%86%E9%A2%91%E6%B5%81%E5%9C%B0%E5%9D%80_web%E7%AB%AF)
 * ```
 * {
 *   "code": 0,
 *   "message": "0",
 *   "ttl": 1,
 *   "data": {
 *     "from": "local",
 *     "result": "suee",
 *     "message": "",
 *     "quality": 64,
 *     "format": "flv720",
 *     "timelength": 283801,
 *     "accept_format": "hdflv2,flv,flv720,flv480,mp4",
 *     "accept_description": [
 *       "高清 1080P+",
 *       "高清 1080P",
 *       "高清 720P",
 *       "清晰 480P",
 *       "流畅 360P"
 *     ],
 *     "accept_quality": [
 *       112,
 *       80,
 *       64,
 *       32,
 *       16
 *     ],
 *     "video_codecid": 7,
 *     "seek_param": "start",
 *     "seek_type": "offset",
 *     "durl": [
 *       {
 *         "order": 1,
 *         "length": 283801,
 *         "size": 70486426,
 *         "ahead": "",
 *         "vhead": "",
 *         "url": "https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/08/62/171776208/171776208_nb2-1-64.flv?e=ig8euxZM2rNcNbNMnwdVhwdlhbK3hwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1662808778&gen=playurlv2&os=cosbv&oi=3719461929&trid=31dc1934e77141bfbdf5ae88aca0b29fu&mid=0&platform=pc&upsig=a4d5f1713e1ba313041d034a958c2414&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=1&bw=249068&logo=80000000",
 *         "backup_url": [
 *           "https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/08/62/171776208/171776208_nb2-1-64.flv?e=ig8euxZM2rNcNbNMnwdVhwdlhbK3hwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1662808778&gen=playurlv2&os=cosbv&oi=3719461929&trid=31dc1934e77141bfbdf5ae88aca0b29fu&mid=0&platform=pc&upsig=a4d5f1713e1ba313041d034a958c2414&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=1&bw=249068&logo=40000000",
 *           "https://upos-sz-mirrorcosb.bilivideo.com/upgcxcode/08/62/171776208/171776208_nb2-1-64.flv?e=ig8euxZM2rNcNbNMnwdVhwdlhbK3hwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1662808778&gen=playurlv2&os=cosbbv&oi=3719461929&trid=31dc1934e77141bfbdf5ae88aca0b29fu&mid=0&platform=pc&upsig=7b8a6924948864944815ec0748cc108f&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=1&bw=249068&logo=40000000"
 *         ]
 *       }
 *     ],
 *     "support_formats": [
 *       {
 *         "quality": 112,
 *         "format": "hdflv2",
 *         "new_description": "1080P 高码率",
 *         "display_desc": "1080P",
 *         "superscript": "高码率",
 *         "codecs": null
 *       },
 *       {
 *         "quality": 80,
 *         "format": "flv",
 *         "new_description": "1080P 高清",
 *         "display_desc": "1080P",
 *         "superscript": "",
 *         "codecs": null
 *       },
 *       {
 *         "quality": 64,
 *         "format": "flv720",
 *         "new_description": "720P 高清",
 *         "display_desc": "720P",
 *         "superscript": "",
 *         "codecs": null
 *       },
 *       {
 *         "quality": 32,
 *         "format": "flv480",
 *         "new_description": "480P 清晰",
 *         "display_desc": "480P",
 *         "superscript": "",
 *         "codecs": null
 *       },
 *       {
 *         "quality": 16,
 *         "format": "mp4",
 *         "new_description": "360P 流畅",
 *         "display_desc": "360P",
 *         "superscript": "",
 *         "codecs": null
 *       }
 *     ],
 *     "high_format": null,
 *     "last_play_time": 0,
 *     "last_play_cid": 0
 *   }
 * }
 * ```
 */
@Serializable
data class VideoPlayDetails(
    @SerialName("accept_description")
    val acceptDescription: List<String> = listOf(),
    @SerialName("accept_format")
    val acceptFormat: String = "", // mp4,mp4
    @SerialName("accept_quality")
    val acceptQuality: List<Int> = listOf(),
    @SerialName("durl")
    val durl: List<Durl> = listOf(),
    @SerialName("format")
    val format: String = "", // mp4
    @SerialName("from")
    val from: String = "", // local
    @SerialName("last_play_cid")
    val lastPlayCid: Int = 0, // 0
    @SerialName("last_play_time")
    val lastPlayTime: Int = 0, // 0
    @SerialName("message")
    val message: String = "",
    @SerialName("quality")
    val quality: Int = 0, // 16
    @SerialName("result")
    val result: String = "", // suee
    @SerialName("seek_param")
    val seekParam: String = "", // start
    @SerialName("seek_type")
    val seekType: String = "", // second
    @SerialName("support_formats")
    val supportFormats: List<SupportFormat> = listOf(),
    @SerialName("timelength")
    val timelength: Int = 0, // 233643
    @SerialName("video_codecid")
    val videoCodecid: Int = 0 // 7
)

@Serializable
data class Durl(
    @SerialName("ahead")
    val ahead: String = "",
    @SerialName("backup_url")
    val backupUrl: List<String> = listOf(),
    @SerialName("length")
    val length: Int = 0, // 233643
    @SerialName("order")
    val order: Int = 0, // 1
    @SerialName("size")
    val size: Int = 0, // 12448260
    @SerialName("url")
    val url: String = "", // https://xy123x246x198x205xy.mcdn.bilivideo.cn:4483/upgcxcode/72/03/429140372/429140372-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1668752789&gen=playurlv2&os=mcdn&oi=1781509104&trid=0000097663fae882444ea5fdb03b1425fb30u&mid=351201307&platform=pc&upsig=98cd9cd8ed75a08f6219513dfbcbef37&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=1002547&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=53426&logo=A0000001
    @SerialName("vhead")
    val vhead: String = ""
)
