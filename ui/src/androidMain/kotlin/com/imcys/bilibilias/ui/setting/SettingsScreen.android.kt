package com.imcys.bilibilias.ui.setting

import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.imcys.bilibilias.BuildConfig
import com.imcys.bilibilias.core.io.resolve
import com.imcys.bilibilias.core.io.toFile
import kotlinx.io.files.Path

@Composable
internal actual fun ShareLogFile() {
    val context = LocalContext.current
    Section(
        title = { Text("分享日志文件") },
        icon = { Icon(Icons.Outlined.Feedback, null) }
    ) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
        // todo use buildconfig
        shareIntent.putExtra(
            Intent.EXTRA_STREAM,
            FileProvider.getUriForFile(
                context,
                "${context.applicationInfo.packageName}.fileprovider",
                getCurrentLogFile().toFile(),
            ),
        )
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(Intent.createChooser(shareIntent, "分享日志文件"))
    }
}

private fun getCurrentLogFile(): Path {
    return BuildConfig.LOG_DIR.resolve("app.log")
}