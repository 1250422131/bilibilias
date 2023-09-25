package com.imcys.bilibilias.common.base.adapter;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class ImageViewAttrAdapter {
    @BindingAdapter({"android:imageUrl"})
    public static void loadImage(ImageView imageView, String url) {
        //Glide设置图片圆角角度
        Glide.with(imageView.getContext())
                .load(url)
                //启用缓存数据
                //.apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(imageView);
    }
}