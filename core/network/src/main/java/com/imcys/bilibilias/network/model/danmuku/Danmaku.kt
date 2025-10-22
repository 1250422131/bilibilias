package com.imcys.bilibilias.network.model.danmuku

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DmSegMobileReq(
    /** 视频所属平台或项目ID */
    val pid: Long? = null,
    /** 弹幕所属对象ID（例如播放项ID） */
    val oid: Long,
    /** 请求类型，默认 1 */
    val type: Int = 1,
    /** 分段索引 */
    @SerialName("segment_index")
    val segmentIndex: Long,
    /** 青少年模式开关，0/1 */
    @SerialName("teenagers_mode")
    val teenagersMode: Int = 0,
)

@Serializable
data class DmSegMobileReply(
    val elems: List<DanmakuElem> = emptyList(),
    val state: Int = 0,
)

@Serializable
data class DanmakuElem(
    /** 弹幕唯一ID */
    val id: Long,
    /** 播放进度（毫秒） */
    val progress: Int = 0,
    /** 显示模式 */
    val mode: Int,
    /** 字号 */
    @SerialName("fontsize")
    val fontSize: Int,
    /** 颜色（无符号整型） */
    val color: UInt,
    /** 发送者 mid 的哈希值 */
    val midHash: String,
    /** 弹幕内容 */
    val content: String,
    /** 创建时间（时间戳） */
    @SerialName("ctime")
    val createTime: Long,
    /** 权重 */
    val weight: Int,
    /** 动作（可选） */
    val action: String? = null,
    /** 弹幕池 */
    val pool: Int = 0,
    /** 字符串形式的ID */
    val idStr: String,
    /** 属性位 */
    val attr: Int = 0,
    /** 动画信息（可选） */
    val animation: String? = null
)
