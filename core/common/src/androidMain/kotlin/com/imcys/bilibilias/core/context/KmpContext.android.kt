package com.imcys.bilibilias.core.context

import android.content.Context
import kotlinx.io.files.Path
import java.lang.ref.WeakReference

actual object KmpContext {
    private lateinit var refs: WeakReference<Context>

    internal fun setUp(context: Context) {
        refs = WeakReference(context)
    }

    fun get(): Context {
        return refs.get()?.applicationContext
            ?: throw IllegalStateException("Context is not initialized")
    }

    actual val isDebug: Boolean = true
    actual val platform: Platform = Platform.ANDROID
}

actual val KmpContext.cacheDir: Path
    get() = Path(get().cacheDir.absolutePath)
actual val KmpContext.dataDir: Path
    get() = Path(get().dataDir.absolutePath)
actual val KmpContext.dataStoreDir: Path
    get() = Path(get().filesDir.absolutePath, "datastore")
actual val KmpContext.logsDir: Path
    get() = Path(get().filesDir.absolutePath, "logs")