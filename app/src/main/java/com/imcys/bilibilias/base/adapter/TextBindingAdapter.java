package com.imcys.bilibilias.base.adapter;

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


    //快速解决字体颜色问题
    @BindingAdapter(value = {"android:textColor", "android:text"})
    public static void setTextColor(TextView textView, String textColor, String text) {
        textView.setText(Html.fromHtml("<font color=\"" + textColor + "\">" + text + "</font> "));
    }


}
