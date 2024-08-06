package com.imcys.bilibilias.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.aakira.napier.Napier
import java.io.File
import javax.inject.Inject

class Logging @Inject constructor(@ApplicationContext private val context: Context) {
    operator fun invoke() {
        try {
            val logFile = File(context.externalCacheDir, "log.txt")
            val oldLogFile = File(context.externalCacheDir, "old_log.txt")
            if (logFile.exists()) {
                if (oldLogFile.exists()) {
                    oldLogFile.delete()
                }
                logFile.renameTo(oldLogFile)
            }
            logFile.delete()
            logFile.createNewFile()
            Runtime.getRuntime().exec(
                arrayOf(
                    "logcat",
                    "-T",
                    "100",
                    "-f",
                    logFile.absolutePath,
                ),
            )
        } catch (e: Exception) {
            Napier.d(e) { "初始化日志" }
        }
    }
}
