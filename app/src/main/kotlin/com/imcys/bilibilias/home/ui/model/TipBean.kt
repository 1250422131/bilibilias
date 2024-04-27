package com.imcys.bilibilias.home.ui.model

import android.graphics.drawable.Drawable

data class TipBean<T>(
    val title: String,
    val long_title: String,
    val doc: String,
    val face: Drawable,
    val link: String = "",
    val activity: Class<T>? = null
)