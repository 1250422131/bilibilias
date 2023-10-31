package com.imcys.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 番剧播放类
 * ![获取番剧视频流URL](https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/bangumi/videostream_url.md#%E8%8E%B7%E5%8F%96%E7%95%AA%E5%89%A7%E8%A7%86%E9%A2%91%E6%B5%81url)
 * ```
 * {
 *   "code": 0,
 *   "message": "success",
 *   "result": {
 *     "accept_format": "mp4480,mp4",
 *     "code": 0,
 *     "seek_param": "start",
 *     "is_preview": 0,
 *     "fnval": 0,
 *     "video_project": true,
 *     "fnver": 0,
 *     "type": "MP4",
 *     "bp": 0,
 *     "result": "suee",
 *     "seek_type": "second",
 *     "from": "local",
 *     "video_codecid": 7,
 *     "record_info": {
 *       "record_icon": "",
 *       "record": ""
 *     },
 *     "durl": [
 *       {
 *         "size": 110479298,
 *         "ahead": "",
 *         "length": 1394078,
 *         "vhead": "",
 *         "backup_url": [
 *           "https://upos-sz-mirrorcoso1.bilivideo.com/upgcxcode/01/96/3629601/3629601_da8-1-160.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1695379296&gen=playurlv2&os=coso1bv&oi=1974978899&trid=21063a33a6e2477b8aa0c268031ed588p&mid=0&platform=pc&upsig=618b905d06233d659498ef42307d1100&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=4513CC66-E383-5740-1B4F-FE8A0C32FABC56433infoc&build=0&f=p_0_0&agrr=1&bw=79253&logo=40000000",
 *           "https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/01/96/3629601/3629601_da8-1-160.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1695379296&gen=playurlv2&os=cosbv&oi=1974978899&trid=21063a33a6e2477b8aa0c268031ed588p&mid=0&platform=pc&upsig=00670235e563725dd3bec0f63e05f4f9&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=4513CC66-E383-5740-1B4F-FE8A0C32FABC56433infoc&build=0&f=p_0_0&agrr=1&bw=79253&logo=40000000"
 *         ],
 *         "url": "https://xy111x30x37x229xy.mcdn.bilivideo.cn:4483/upgcxcode/01/96/3629601/3629601_da8-1-160.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1695379296&gen=playurlv2&os=mcdn&oi=1974978899&trid=000021063a33a6e2477b8aa0c268031ed588p&mid=0&platform=pc&upsig=03faeec9dcfa8c0faf4ce4275f9e145f&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=1002663&bvc=vod&nettype=0&orderid=0,3&buvid=4513CC66-E383-5740-1B4F-FE8A0C32FABC56433infoc&build=0&f=p_0_0&agrr=1&bw=79253&logo=A0000001",
 *         "order": 1,
 *         "md5": ""
 *       }
 *     ],
 *     "is_drm": false,
 *     "no_rexcode": 0,
 *     "format": "mp4480",
 *     "support_formats": [
 *       {
 *         "display_desc": "480P",
 *         "superscript": "",
 *         "codecs": [
 *
 *         ],
 *         "format": "mp4480",
 *         "description": "清晰 480P",
 *         "quality": 32,
 *         "new_description": "480P 清晰"
 *       },
 *       {
 *         "display_desc": "360P",
 *         "superscript": "",
 *         "codecs": [
 *
 *         ],
 *         "format": "mp4",
 *         "description": "流畅 360P",
 *         "quality": 16,
 *         "new_description": "360P 流畅"
 *       }
 *     ],
 *     "message": "",
 *     "accept_quality": [
 *       32,
 *       16
 *     ],
 *     "quality": 32,
 *     "timelength": 1394078,
 *     "has_paid": false,
 *     "clip_info_list": [
 *       {
 *         "materialNo": 0,
 *         "start": 65,
 *         "end": 156,
 *         "toastText": "即将跳过片头",
 *         "clipType": "CLIP_TYPE_OP"
 *       },
 *       {
 *         "materialNo": 0,
 *         "start": 1294,
 *         "end": 1394,
 *         "toastText": "即将跳过片尾",
 *         "clipType": "CLIP_TYPE_ED"
 *       }
 *     ],
 *     "accept_description": [
 *       "清晰 480P",
 *       "流畅 360P"
 *     ],
 *     "status": 2
 *   }
 * }
 * ```
 */
@Serializable
data class BangumiPlayBean(
    @SerialName("code")
    val code: Int = 0,
    @SerialName("message")
    val message: String = "",
    @SerialName("result")
    val result: Result = Result()
) {
    @Serializable
    data class Result(
        @SerialName("accept_description")
        val acceptDescription: List<String> = listOf(),
        @SerialName("accept_format")
        val acceptFormat: String = "",
        @SerialName("accept_quality")
        val acceptQuality: List<Int> = listOf(),
        @SerialName("bp")
        val bp: Int = 0,
        @SerialName("clip_info_list")
        val clipInfoList: List<ClipInfo> = listOf(),
        @SerialName("code")
        val code: Int = 0,
        @SerialName("durl")
        val durl: List<Durl> = listOf(),
        @SerialName("fnval")
        val fnval: Int = 0,
        @SerialName("fnver")
        val fnver: Int = 0,
        @SerialName("format")
        val format: String = "",
        @SerialName("from")
        val from: String = "",
        @SerialName("has_paid")
        val hasPaid: Boolean = false,
        @SerialName("is_drm")
        val isDrm: Boolean = false,
        @SerialName("is_preview")
        val isPreview: Int = 0,
        @SerialName("message")
        val message: String = "",
        @SerialName("no_rexcode")
        val noRexcode: Int = 0,
        @SerialName("quality")
        val quality: Int = 0,
        @SerialName("record_info")
        val recordInfo: RecordInfo = RecordInfo(),
        @SerialName("result")
        val result: String = "",
        @SerialName("seek_param")
        val seekParam: String = "",
        @SerialName("seek_type")
        val seekType: String = "",
        @SerialName("status")
        val status: Int = 0,
        @SerialName("support_formats")
        val supportFormats: List<SupportFormat> = listOf(),
        @SerialName("timelength")
        val timelength: Int = 0,
        @SerialName("type")
        val type: String = "",
        @SerialName("video_codecid")
        val videoCodecid: Int = 0,
        @SerialName("video_project")
        val videoProject: Boolean = false
    ) {
        @Serializable
        data class ClipInfo(
            @SerialName("clipType")
            val clipType: String = "",
            @SerialName("end")
            val end: Int = 0,
            @SerialName("materialNo")
            val materialNo: Int = 0,
            @SerialName("start")
            val start: Int = 0,
            @SerialName("toastText")
            val toastText: String = ""
        )

        @Serializable
        data class Durl(
            @SerialName("ahead")
            val ahead: String = "",
            @SerialName("backup_url")
            val backupUrl: List<String> = listOf(),
            @SerialName("length")
            val length: Int = 0,
            @SerialName("md5")
            val md5: String = "",
            @SerialName("order")
            val order: Int = 0,
            @SerialName("size")
            val size: Int = 0,
            @SerialName("url")
            val url: String = "",
            @SerialName("vhead")
            val vhead: String = ""
        )

        @Serializable
        data class RecordInfo(
            @SerialName("record")
            val record: String = "",
            @SerialName("record_icon")
            val recordIcon: String = ""
        )

        @Serializable
        data class SupportFormat(
            @SerialName("description")
            val description: String = "",
            @SerialName("display_desc")
            val displayDesc: String = "",
            @SerialName("format")
            val format: String = "",
            @SerialName("new_description")
            val newDescription: String = "",
            @SerialName("quality")
            val quality: Int = 0,
            @SerialName("superscript")
            val superscript: String = ""
        )
    }
}
