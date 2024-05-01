package com.imcys.bilibilias.base.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import com.google.android.material.textview.MaterialTextView

/**
 * <h2>选框文本视图</h2>
 *
 * @author 紫叶工作室
 */
class MarqueeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialTextView(context, attrs, defStyleAttr) {
    init {
        // 设置启用水平淡入淡出边缘
        isHorizontalFadingEdgeEnabled = true
        // 设置省略尺寸
        ellipsize = TextUtils.TruncateAt.MARQUEE
        // 设置选框重复限制
        marqueeRepeatLimit = -1
        // 设置单行
        setSingleLine(true)
    }

    override fun isFocused(): Boolean {
        return true
    }
}