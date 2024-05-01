package com.imcys.bilibilias.common.base.adapter

import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.imcys.bilibilias.common.base.view.AsJzvdStd

/**
 * @author:imcys
 * @create: 2022-11-18 17:39
 * @Description: 饺子播放器绑定器
 */
object AsJzvdStdAdapter {
    @BindingAdapter("android:imageUrl")
    fun loadImage(asJzvdStd: AsJzvdStd, url: String?) {
        asJzvdStd.posterImageUrl = url
        Glide.with(asJzvdStd.context)
            .load(url)
            .into(asJzvdStd.posterImageView)
    }
}
