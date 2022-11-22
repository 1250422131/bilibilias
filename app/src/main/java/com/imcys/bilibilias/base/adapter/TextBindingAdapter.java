package com.imcys.bilibilias.base.adapter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingConversion;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;

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


    @BindingAdapter(value = {"html"})
    public static void setTextHtml(TextView textView, String html) {
        textView.setText(Html.fromHtml(html));
    }


}

