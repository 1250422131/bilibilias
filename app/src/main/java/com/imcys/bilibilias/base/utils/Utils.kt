package com.imcys.bilibilias.base.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.startActivity(clazz: Class<*>) {
    val intent = Intent(this, clazz)
    startActivity(intent)
}

fun Context.openUri(uri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW, uri)
    // 如果不支持的 Uri 将抛出异常
    startActivity(intent)
}
