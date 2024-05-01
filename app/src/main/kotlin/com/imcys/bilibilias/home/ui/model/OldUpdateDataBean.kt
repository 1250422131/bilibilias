package com.imcys.bilibilias.home.ui.model

import kotlinx.serialization.Serializable
/**
 * code : 0 version : 2.2 gxnotice : 更新有问题联系
 *
 * QQ1250422131
 *
 * 1.1.0更新
 *
 * 为了对抗盗版，我们不得不进行更新。
 *
 * 1.支持批量下载
 *
 * 2.全局UI美化
 *
 * 3.支持UP主视频冻结【保护UP视频】
 *
 * 4.支持桌面小部件【粉丝显示器】 notice :
 * bilibilias全程采用B站自身服务器处理下载，严禁使用该程序做为非法用途，一切后果由使用者自行承担。
 *
 * 特别注意！！！B站UP们视频创作都非常不易，大多数视频都写了禁止二次转载，所以我们严禁用户使用下载的视频进行二次发布，或者转载时以任何方式抹除，不标注，不在显眼处标注来源B站链接，UP等视频相关信息，当然你可以争取UP主同意后使用，违反者由使用者承担一切后果，这包括法律责任，使用本程序代表同意且接受本公告所写责任。
 *
 * 我们这里的画质是根据你是否有会员来判断的，我们知道，有些视频较为高的画质需要会员缓存，那么在bilibilias，你必须登录会员账号才能下载，否则无法获取下载地址。
 * 严禁二改本程序，本程序免费使用，严禁用于商业用途！！！！ 软件仅供学习交流，请在24小时内删除。 使用本程序代表同意上述内容 url :
 * https://api.misakamoe.com/app/ APKMD5 : 0 APKToKen : 0 APKToKenCR : 0 ID
 *
 * @description: 旧的后端更新类
 */
@Serializable
data class OldUpdateDataBean(
    val code: Int = 0,
    val version: String = "",
    val gxnotice: String = "",
    val notice: String = "",
    val url: String = "",
    val apkmD5: String = "",
    val apkToKen: String = "",
    val apkToKenCR: String = "",
    val id: String = "",
    val gray:Boolean = false,
)
