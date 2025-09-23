package com.imcys.bilibilias.common.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun Context.openLink(url: String) {
    val intent = Intent().apply {
        action = "android.intent.action.VIEW"
        data = url.toUri()
    }
    startActivity(intent)
}