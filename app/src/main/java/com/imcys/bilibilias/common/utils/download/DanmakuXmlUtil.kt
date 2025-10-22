package com.imcys.bilibilias.common.utils.download

import com.imcys.bilibilias.network.model.danmuku.DanmakuElem
import com.imcys.bilibilias.network.model.danmuku.DmSegMobileReply


object DanmakuXmlUtil {
    /**
     * 构建弹幕XML头部（不含 <i> 结尾）
     */
    fun buildXmlHeader(chatId: Long = 0L): String {
        return buildString {
            append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
            append("<i>\n")
            append("<chatserver>chat.bilibili.com</chatserver>\n")
            append("<chatid>").append(chatId).append("</chatid>\n")
            append("<mission>0</mission>\n")
            append("<maxlimit>1000</maxlimit>\n")
            append("<state>0</state>\n")
            append("<real_name>0</real_name>\n")
            append("<source>k-v</source>\n")
        }
    }

    /**
     * 构建单条弹幕的 <d> 节点
     */
    fun buildDanmakuNode(elem: DanmakuElem): String {
        val p = buildString {
            append(String.format("%.5f", elem.progress / 1000.0))
            append(",")
            append(elem.mode)
            append(",")
            append(elem.fontSize)
            append(",")
            append(elem.color.toLong() and 0xFFFFFFFF)
            append(",")
            append(elem.createTime)
            append(",0,")
            append(elem.midHash)
            append(",")
            append(elem.idStr)
            append(",10")
        }
        return "<d p=\"$p\">${escapeXml(elem.content)}</d>\n"
    }

    /**
     * 构建所有弹幕内容节点
     */
    fun buildDanmakuNodes(elems: List<DanmakuElem>): String =
        elems.joinToString(separator = "") { buildDanmakuNode(it) }

    /**
     * 构建结尾 </i>
     */
    fun buildXmlFooter(): String = "</i>"

    /**
     * 完整构建弹幕XML（适合一次性全部弹幕）
     */
    fun toBilibiliDanmakuXml(reply: DmSegMobileReply, chatId: Long = 0L): String {
        return buildXmlHeader(chatId) + buildDanmakuNodes(reply.elems) + buildXmlFooter()
    }

    /**
     * 完整构建弹幕XML（适合一次性全部弹幕）
     */
    fun toBilibiliDanmakuXml(elems: List<DanmakuElem>, chatId: Long = 0L): String {
        return buildXmlHeader(chatId) + buildDanmakuNodes(elems) + buildXmlFooter()
    }

    /**
     * 简单的XML内容转义
     */
    private fun escapeXml(text: String): String =
        text.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;")
}
