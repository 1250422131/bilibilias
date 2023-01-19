package com.imcys.bilibilias.adapter;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.imcys.bilibilias.base.view.AsJzvdStd;

/**
 * @author:imcys
 * @create: 2022-11-18 17:39
 * @Description: 饺子播放器绑定器
 */
public class AsJzvdStdAdapter {

    @BindingAdapter({"android:imageUrl"})
    public static void loadImage(AsJzvdStd asJzvdStd, String url) {
        asJzvdStd.setPosterImageUrl(url);
        Glide.with(asJzvdStd.getContext())
                .load(url)
                .into(asJzvdStd.posterImageView);
    }


}
