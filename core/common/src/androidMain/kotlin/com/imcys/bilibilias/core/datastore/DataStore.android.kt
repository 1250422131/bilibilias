package com.imcys.bilibilias.core.datastore

import androidx.datastore.dataStoreFile
import com.imcys.bilibilias.core.context.KmpContext
import com.imcys.bilibilias.core.io.SystemPath
import com.imcys.bilibilias.core.io.inSystem
import kotlinx.io.files.Path

actual fun resolveDataStoreFile(name: String): SystemPath {
    return Path(
        KmpContext.get()
            .applicationContext
            .dataStoreFile(name)
            .absolutePath
    )
        .inSystem
}