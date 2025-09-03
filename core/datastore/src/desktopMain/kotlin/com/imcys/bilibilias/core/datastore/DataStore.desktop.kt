package com.imcys.bilibilias.core.datastore

import com.imcys.bilibilias.BuildConfig
import com.imcys.bilibilias.core.io.SystemPath
import com.imcys.bilibilias.core.io.inSystem
import com.imcys.bilibilias.core.io.resolve

actual fun resolveDataStoreFile(name: String): SystemPath {
    return BuildConfig.DATASTORE_DIR.resolve(name).inSystem
}