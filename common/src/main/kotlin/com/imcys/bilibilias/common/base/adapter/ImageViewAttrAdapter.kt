package com.imcys.bilibilias.common.base.adapter

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter

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

    }

    @BindingAdapter("android:imageUrl")
    fun setImageUrl(imageView: ImageView, url: String?) {
    }

    @BindingAdapter("isSelected")
    fun isSelected(imageView: ImageView, selected: Boolean?) {
        imageView.setSelected(selected!!)
    }
}