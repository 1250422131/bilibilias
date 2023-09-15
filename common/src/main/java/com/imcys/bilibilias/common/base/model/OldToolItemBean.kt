package com.imcys.bilibilias.common.base.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * ![工具](https://api.misakamoe.com/app/AppFunction.php?type=oldToolItem)
 * ```
 * {
 *   "code": 0,
 *   "data": [
 *     {
 *       "tool_code": 1,
 *       "color": "#efb336",
 *       "title": "缓存视频",
 *       "img_url": "https://s1.ax1x.com/2023/08/24/pPY4Q6H.png"
 *     },
 *     {
 *       "tool_code": 3,
 *       "color": "#fb7299",
 *       "title": "网页解析",
 *       "img_url": "https://s1.ax1x.com/2023/02/04/pSyHEy6.png"
 *     },
 *     {
 *       "tool_code": 4,
 *       "color": "#fb7299",
 *       "title": "日志导出",
 *       "img_url": "https://s1.ax1x.com/2023/02/05/pS6IsAJ.png"
 *     },
 *     {
 *       "tool_code": 2,
 *       "color": "#fb7299",
 *       "title": "关于设置",
 *       "img_url": "https://s1.ax1x.com/2023/01/20/pSGSJGF.png"
 *     }
 *   ]
 * }
 * ```
 */
@Serializable
data class OldToolItemBean(
    @SerialName("color")
    val color: String = "", // #efb336
    @SerialName("img_url")
    val imgUrl: String = "", // https://s1.ax1x.com/2023/08/24/pPY4Q6H.png
    @SerialName("title")
    val title: String = "", // 缓存视频
    @SerialName("tool_code")
    val toolCode: Int = 0 // 1
)
