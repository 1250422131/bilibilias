package com.imcys.bilibilias.common.base.adapter;

import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;

import com.airbnb.lottie.LottieAnimationView;

/**
 * @author:imcys
 * @create: 2022-12-07 10:19
 * @Description: LottieAnimation的数据适配器
 */
@BindingMethods({
        @BindingMethod(type = LottieAnimationView.class, attribute = "lottie_progress", method = "setProgress"),
})
public class LottieAnimationViewAdapter {

}
