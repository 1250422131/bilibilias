package com.imcys.bilibilias.base.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textview.MaterialTextView;

/**
 * <h2>选框文本视图</h2>
 *
 * @author 紫叶工作室
 */
public class MarqueeTextView extends MaterialTextView {

    public MarqueeTextView(@NonNull Context context) {
        this(context, null);
    }

    public MarqueeTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarqueeTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 设置启用水平淡入淡出边缘
        setHorizontalFadingEdgeEnabled(true);
        // 设置省略尺寸
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        // 设置选框重复限制
        setMarqueeRepeatLimit(-1);
        // 设置单行
        setSingleLine(true);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}