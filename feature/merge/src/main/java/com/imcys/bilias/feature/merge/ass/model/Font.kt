package com.imcys.bilias.feature.merge.ass.model

/**
 * @param name 使用的字体名称，区分大小写
 * @param size 字体的字号
 * @param bold 它定义了文本是否为粗体。-1 是粗体，0 不是。它与斜体属性相互独立，即你可以把文本同时设置为粗体和斜体
 * @param italic 它定义了文本是否为斜体。-1 是斜体，0 不是。它与粗体属性相互独立，即你可以把文本同时设置为粗体和斜体
 * @param underline 它定义了文本是否有下划线。-1 有，0 没有
 * @param strikeOut 它定义了文本是否有中划线（删除线）。-1 有，0 没有
 * @param scaleX 修改字体的宽度（为百分数）
 * @param scaleY 修改字体的高度（为百分数）
 * @param spacing 字符之间额外的间隙（为像素）
 * @param angle 旋转所基于的原点由 Alignment 定义（为角度）
 * @param borderStyle 边框的样式。1 为边框 + 阴影，3 为不透明背景。可以被 \r 重置
 * @param outline 如果 BorderStyle 为 1，它定义了文字边框的像素宽度。常见的值有 0、1、2、3 或 4
 * @param shadow 如果 BorderStyle 为 1，它定义了文字阴影的像素深度。 常见的值有 0、1、2、3 或4
 *     阴影总是基于边框使用，事实上阴影和边框可以独立使用，互不影响。
 * @param alignment 它设置文本如何在屏幕上根据左右边距对齐和垂直位置。 可能的值有 1 = 居左，2 = 居中，3 =居右。
 *     上述的值加 4 出现在屏幕顶部，上述的值加 8 出现在屏幕中间。 例如，5 = 屏幕顶部居左。但是在
 *     ASS中是按数字键盘对应的位置（1-3 为底部，4-6 为中部，7-9 为顶部）。在 ASS 中与 \an 一致。
 * @param encoding 它定义了字体的字符集或编码方式。在多语种的 Windows 安装中它可以获取多种语言中的字符。 通常 0
 *     为英文，134 为简体中文，136 为繁体中文。当文件是 Unicode 编码时，该字段在解析对话时会起作用。
 * @param outlineColor 在某条字幕为了防止重叠而自动移动时可能会使用该颜色而不是 PrimaryColour 以区分不同的字幕
 *     在 ASS 中名称为 OutlineColor，设置字体边框的颜色
 * @param backColor 设置字体阴影的颜色。
 */
data class Font(
    val name: String = "Arial",
    val size: Int = 0,
    val bold: Int = 0,
    val italic: Int = 0,
    val underline: Int = 0,
    val strikeOut: Int = 0,
    val scaleX: Int = 100,
    val scaleY: Int = 100,
    val spacing: Float = 0.0f,
    val angle: Float = 0.0f,
    val borderStyle: Int = 1,
    val outline: Int = 1,
    val shadow: Int = 0,
    val alignment: Int = 2,
    val encoding: Int = 1,

    val outlineColor: Long = 0X00000000,
    val backColor: Long = 0X00000000,
)

internal val ya_hei_36 = Font("Microsoft YaHei", 36)
internal val ya_hei_52 = Font("Microsoft YaHei", 52)
internal val ya_hei_64 = Font("Microsoft YaHei", 64)
internal val ya_hei_72 = Font("Microsoft YaHei", 72)
internal val ya_hei_90 = Font("Microsoft YaHei", 90)