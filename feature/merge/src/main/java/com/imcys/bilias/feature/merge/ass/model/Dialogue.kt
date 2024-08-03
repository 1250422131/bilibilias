package com.imcys.bilias.feature.merge.ass.model

fun buildDialogue(
    start: String,
    end: String,
    style: V4Style,
    effect: String,
    text: String,
): String {
    return Dialogue(start, end, style, effect, text).toString()
}

data class Dialogue(
    val start: String,
    val end: String,
    val style: V4Style,
    val effect: String,
    val text: String,
    val layer: Int = 3,
    val region: Region = Region(),
) {
    override fun toString(): String {
        return "$layer, ${start}, ${end}, ${style.name}, /* name */, " +
                "${region.marginL}, ${region.marginR}, ${region.marginV}, " +
                "$effect, $text"
    }
}

/**
 * \move(<x1>,<y1>,<x2>,<y2>[,<t1>,<t2>]) 提供字幕的移动效果。<x1>,<y1>
 * 是开始点坐标，<x2>,<y2> 是结束点坐标。 <t1> 和 <t2> 是相对于字幕显示时间的开始运动与结束运动的毫秒时间。
 *
 * 在 <t1> 之前，字幕定位在 <x1>,<y1>。 在 <t1> 与 <t2> 之间，字幕从 <x1>,<y1> 均速移动到
 * <x2>,<y2>。 在 <t2> 之后，字幕定位在 <x2>,<y2>。 当 <t1> 和 <t2> 没写或者都是 0
 * 时，则在字幕的整段时间内均速移动。 当一行中有多个 \pos 和 \move 时，以第一个为准。 当 \move 和 Effect
 * 效果同时存在时，结果比较迷。 当一行中含有 \move 时会忽略字幕重叠冲突的检测。
 */
data class Effect(private val type: String) {
    override fun toString(): String {
        return "Effect(type='$type')"
    }
}