package com.imcys.bilibilias.common.base.adapter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.databinding.BindingConversion;

public class ButtonAdapter {

    @BindingConversion
    public static ColorDrawable setBackground(int color) {
        return new ColorDrawable(color);
    }

    @BindingConversion
    public static ColorDrawable setBackground(String colorStr) {
        if (colorStr.equals("")) colorStr = "#000000";
        int color = Color.parseColor(colorStr);
        return new ColorDrawable(color);

    }

}