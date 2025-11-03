package com.imcys.bilibilias.common.utils


import com.imcys.bilibilias.R
import androidx.compose.ui.res.stringResource
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri
import com.imcys.bilibilias.BuildConfig

fun Context.openLink(url: String) {
    val intent = Intent().apply {
        action = "android.intent.action.VIEW"
        data = url.toUri()
    }
    startActivity(intent)
}

fun String.copyText(context: Context, title: String) {
    val clipboard =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(title, this)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, stringResource(R.string.tools_已复制), Toast.LENGTH_SHORT).show()
}

inline fun analyticsSafe(action: () -> Unit) {
    if (BuildConfig.ENABLED_ANALYTICS){
        action()
    }
}

fun isEnabledAnalytics(): Boolean {
    return BuildConfig.ENABLED_ANALYTICS
}