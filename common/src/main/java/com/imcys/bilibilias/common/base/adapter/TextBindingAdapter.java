package com.imcys.bilibilias.common.base.adapter;

import android.graphics.Color;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingConversion;

import com.imcys.bilibilias.common.base.extend.StringKt;

/**
 * @author:imcys
 * @create: 2022-11-16 10:19
 * @Description: 文本视图绑定器
 */


public class TextBindingAdapter {


    @BindingConversion
    public static int setTextColor(String textColor) {
        if (textColor.equals("")) textColor = "#000000";
        return Color.parseColor(textColor);
    }

    @BindingConversion
    public static String setText(int text) {
        return String.valueOf(text);
    }


    @BindingAdapter(value = {"html"})
    public static void setTextHtml(TextView textView, String html) {
        textView.setText(StringKt.toHtml(html));
    }


}

