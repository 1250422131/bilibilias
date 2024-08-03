package com.imcys.bilias.feature.merge.ass.model

import com.imcys.bilias.feature.merge.ass.colorFormat

fun buildStyles(): String {
    return """
[V4+ Styles]    
Format: Name, Fontname, Fontsize, PrimaryColour, SecondaryColour, OutlineColour, BackColour, Bold, Italic, Underline, StrikeOut, ScaleX, ScaleY, Spacing, Angle, BorderStyle, Outline, Shadow, Alignment, MarginL, MarginR, MarginV, Encoding
$Small
$Medium
$Large
$Larger
$ExtraLarge
    """
}

/** @param name 样式名称（用于 [Events] 部分引用，区分大小写，不能包含逗号） */
@OptIn(ExperimentalStdlibApi::class)
data class V4Style(
    val name: String,
    val font: Font,
    val color: Color = Color(),
    val region: Region = Region(),
) {
    override fun toString(): String {
        return "Style: $name, ${font.name}, ${font.size}, " +
                "${color.primary.toHexString(colorFormat)}, ${color.secondary.toHexString(colorFormat)}, " +
                "${font.outlineColor.toHexString(colorFormat)}, ${font.backColor.toHexString(colorFormat)}, " +
                "${font.bold}, ${font.italic}, ${font.underline}, ${font.strikeOut}, " +
                "${font.scaleX}, ${font.scaleY}, " +
                "${font.spacing}, ${font.angle}, " +
                "${font.borderStyle}, ${font.outline}, ${font.shadow}, ${font.alignment}, " +
                "${region.marginL}, ${region.marginL}, ${region.marginV}, " +
                "${font.encoding}"
    }

    companion object {
        const val SEP = ","
        const val STYLE = "Style: "
        const val FORMAT_STRING = "Name, Fontname, Fontsize, PrimaryColour, " +
                "SecondaryColour, OutlineColour, BackColour, Bold, Italic, Underline, " +
                "StrikeOut, ScaleX, ScaleY, Spacing, Angle, BorderStyle, Outline, Shadow, " +
                "Alignment, MarginL, MarginR, MarginV, Encoding"
    }
}

internal val Small = V4Style("Small", ya_hei_36)
internal val Medium = V4Style("Medium", ya_hei_52)
internal val Large = V4Style("Large", ya_hei_64)
internal val Larger = V4Style("Larger", ya_hei_72)
internal val ExtraLarge = V4Style("ExtraLarge", ya_hei_90)
