package com.imcys.bilibilias.base.adapter;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.imcys.bilibilias.base.view.AsJzvdStd;

/**
 * @author:imcys
 * @create: 2022-11-18 17:39
 * @Description: 饺子播放器绑定器
 */
public class AsJzvdStdAdapter {

    @BindingAdapter({"android:imageUrl"})
    public static void loadImage(AsJzvdStd asJzvdStd, String url) {

        Glide.with(asJzvdStd.getContext())
                .load(url)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(asJzvdStd.posterImageView);

    }
}
