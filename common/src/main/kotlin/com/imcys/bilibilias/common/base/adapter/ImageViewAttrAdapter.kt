package com.imcys.bilibilias.common.base.adapter

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

object ImageViewAttrAdapter {
    @BindingAdapter("android:src")
    fun setSrc(view: ImageView, bitmap: Bitmap?) {
        view.setImageBitmap(bitmap)
    }

    @BindingAdapter("android:src")
    fun setSrc(view: ImageView, resId: Int) {
        view.setImageResource(resId)
    }

    @BindingAdapter("android:imageUrl", "android:imageRoundingRadius")
    fun loadImage(imageView: ImageView, url: String?, imageRoundingRadius: Int) {
        //设置图片上去，就不在多写了
        //Glide设置图片圆角角
        //.apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA))
        val roundedCorners = RoundedCorners(imageRoundingRadius)
        val options = RequestOptions.bitmapTransform(roundedCorners)
        Glide.with(imageView.context)
            .load(url)
            .apply(options)
            .into(imageView)
    }

    @BindingAdapter("android:imageUrl")
    fun setImageUrl(imageView: ImageView, url: String?) {
        //Glide设置图片圆角角度
        Glide.with(imageView.context)
            .load(url) //启用缓存数据
            //.apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
            .into(imageView)
    }

    @BindingAdapter("isSelected")
    fun isSelected(imageView: ImageView, selected: Boolean?) {
        imageView.setSelected(selected!!)
    }
}