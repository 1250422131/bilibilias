package com.imcys.bilibilias.core.download.chore

import android.content.Context
import android.net.Uri
import com.imcys.bilibilias.core.common.utils.get保存路径
import com.lazygeniouz.dfc.file.DocumentFileCompat
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.aakira.napier.Napier
import okio.buffer
import okio.sink
import okio.source
import java.io.File

class MoveFileInterceptor @AssistedInject constructor(
    @ApplicationContext private val context: Context
) : Interceptor<String> {
    override val enable = get保存路径() != null
    override fun intercept(message: String, chain: Interceptor.Chain) {
        Napier.d(tag = "Interceptor") { "移动文件 $enable, $message" }
        if (!enable) return
        val document = DocumentFileCompat.fromTreeUri(context, Uri.parse(get保存路径()))!!
        document.createDirectory("bilibiliAsDownload")?.let { document ->
            val findFile = document.createFile("application/pdf", "材料清单PDF")
            Napier.d("findFile:" + findFile + " uri:" + findFile?.uri)

            findFile?.uri?.let {
                val inBuffer = File(message).source().buffer()
                context.contentResolver.openOutputStream(it)?.let {
                    it.sink().buffer().use {
                        it.writeAll(inBuffer)
                        inBuffer.close()
                    }
                }
            }
        }
    }
}
