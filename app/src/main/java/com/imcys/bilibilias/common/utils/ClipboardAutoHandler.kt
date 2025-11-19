import com.imcys.bilibilias.common.utils.AsRegexUtil

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.imcys.bilibilias.datastore.AppSettings


/**
 * 处理剪贴板自动识别
 */
@Composable
fun ClipboardAutoHandler(
    appSettings: AppSettings,
    shouldHandleClipboard: () -> Boolean = { true },
    onClipboardText: (String) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val enabledState by rememberUpdatedState(appSettings.enabledClipboardAutoHandling)
    val shouldHandleState by rememberUpdatedState(shouldHandleClipboard)
    val onClipboardTextState by rememberUpdatedState(onClipboardText)

    if (!enabledState || !shouldHandleState()) return

    LaunchedEffect(lifecycleOwner, shouldHandleState) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            Log.d("TAG", "ClipboardAutoHandler: RESUMED")
            val text = context.consumeClipboardText()
            if (!text.isNullOrEmpty()) {
                onClipboardTextState(text)
            }
        }
    }
}

private fun Context.consumeClipboardText(): String? {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager ?: return null
    val clip = clipboard.primaryClip ?: return null
    val text = clip.getItemAt(0)
        .coerceToText(this)
        ?.toString()
        ?.trim()
        .takeIf { !it.isNullOrEmpty() }

    if (text.isNullOrBlank() || AsRegexUtil.parse(text) == null) {
        return null
    }
    // 清空，避免重复处理
    clipboard.setPrimaryClip(ClipData.newPlainText("", ""))
    return text
}