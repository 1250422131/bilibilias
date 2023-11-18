package com.imcys.bilibilias.okdownloader.android

import android.content.Context
import com.imcys.bilibilias.okdownloader.Download
import com.imcys.bilibilias.okdownloader.ErrorCode
import com.imcys.bilibilias.okdownloader.Interceptor
import com.imcys.bilibilias.okdownloader.android.internal.DownloadDatabase
import com.imcys.bilibilias.okdownloader.android.internal.RecordEntity
import org.apache.commons.codec.digest.DigestUtils
import java.io.File

class CopyOnExistsInterceptor(
    private val context: Context,
    private val recordCount: Int = 5000
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Download.Response {
        val request = chain.request()
        val record = try {
            DownloadDatabase.getInstance(context).recordDao().queryByUrl(request.url)
        } catch (t: Throwable) {
            // ignore
            null
        }
        if (record != null && record.path != request.path) {
            val file = File(record.path)
            if (file.exists() && file.md5() == record.md5 && !request.destFile().exists()) {
                file.copyTo(request.destFile(), true)
                return Download.Response.Builder()
                    .code(ErrorCode.COPY_SUCCESS)
                    .output(request.destFile())
                    .totalSize(request.destFile().length())
                    .message("Copy file success")
                    .build()
            }
        }
        val response = chain.proceed(request)
        if (response.isSuccessful() && request.md5 != null && request.destFile().exists()) {
            try {
                with(DownloadDatabase.getInstance(context).recordDao()) {
                    insert(request.record())
                    deleteLessThen(getMaxId() - recordCount)
                }
            } catch (t: Throwable) {
                // ignore
            }
        }
        return response
    }

    private fun Download.Request.record(): RecordEntity {
        return RecordEntity(url = url, path = path, md5 = md5!!)
    }
}

internal fun File.md5(): String {
    return DigestUtils.md5Hex(this.readBytes())
}
