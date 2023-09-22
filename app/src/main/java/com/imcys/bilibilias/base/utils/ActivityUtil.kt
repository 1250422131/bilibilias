package com.imcys.bilibilias.base.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

fun Context.startActivity(clazz: Class<out Activity>, bundle: Bundle = Bundle()) {
    val intent = Intent(this, clazz).apply {
        putExtras(bundle)
    }
    startActivity(intent)
}

fun Context.openUri(uri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW, uri)
    // 如果不支持的 Uri 将抛出异常
    startActivity(intent)
}
fun Context.getActivity(): AppCompatActivity? = when (this) {
    is AppCompatActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}
