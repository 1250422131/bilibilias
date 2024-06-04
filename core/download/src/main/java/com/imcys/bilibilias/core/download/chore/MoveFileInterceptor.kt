package com.imcys.bilibilias.core.download.chore

import android.content.Context
import android.net.Uri
import com.imcys.bilibilias.core.common.download.DefaultConfig
import com.imcys.bilibilias.core.datastore.preferences.AsPreferencesDataSource
import com.lazygeniouz.dfc.file.DocumentFileCompat
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okio.buffer
import okio.sink
import okio.source
import java.io.File
import javax.inject.Inject

class MoveFileInterceptor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userPreferences: AsPreferencesDataSource,
) : Interceptor<String> {
    override val enable = runBlocking {
        userPreferences.userData.first().fileStoragePath != DefaultConfig.defaultStorePath
    }

    override fun intercept(message: String, chain: Interceptor.Chain) {
        Napier.d(tag = "Interceptor") { "移动文件 $enable, $message" }
        if (!enable) return
//        runBlocking {
//            DocumentFileCompat.fromTreeUri(
//                context,
//                Uri.parse(userPreferences.userData.first().fileStoragePath)
//            )
//        }?.let { document ->
//            val newFile = document.createFile("video/mp4", message)
//            Napier.d("findFile:" + newFile + " uri:" + newFile?.uri)
//
//            newFile?.uri?.let {
//                val inBuffer = File(message).source().buffer()
//                context.contentResolver.openOutputStream(it)?.let {
//                    it.sink().buffer().use {
//                        it.writeAll(inBuffer)
//                        inBuffer.close()
//                    }
//                }
//            }
//        }
    }
}
