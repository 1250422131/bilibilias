package com.imcys.bilibilias.common.base.adapter;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class ImageViewAttrAdapter {


    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, Bitmap bitmap) {
        view.setImageBitmap(bitmap);
    }

    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, int resId) {
        view.setImageResource(resId);
    }

    @BindingAdapter({"android:imageUrl", "android:imageRoundingRadius"})
    public static void loadImage(ImageView imageView, String url, int imageRoundingRadius) {
        //设置图片上去，就不在多写了
        //Glide设置图片圆角角
        //.apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA))
        RoundedCorners roundedCorners = new RoundedCorners(imageRoundingRadius);
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
        Glide.with(imageView.getContext())
                .load(url)
                .apply(options)
                .into(imageView);

    }

    @BindingAdapter({"android:imageUrl"})
    public static void loadImage(ImageView imageView, String url) {
        //Glide设置图片圆角角度
        Glide.with(imageView.getContext())
                .load(url)
                //启用缓存数据
                //.apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(imageView);
    }

    @BindingAdapter({"isSelected"})
    public static void isSelected(ImageView imageView, Boolean selected) {
        imageView.setSelected(selected);
    }
}