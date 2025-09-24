package com.imcys.bilibilias.common.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.core.net.toUri
import io.ktor.content.TextContent
import kotlin.text.ifEmpty

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
    Toast.makeText(context, "已复制到剪贴板", Toast.LENGTH_SHORT).show()
}