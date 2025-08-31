package com.imcys.bilibilias.core.datastore

import com.imcys.bilibilias.BuildConfig
import com.imcys.bilibilias.core.io.SystemPath
import com.imcys.bilibilias.core.io.inSystem
import kotlinx.io.files.Path

actual fun resolveDataStoreFile(name: String): SystemPath {
    return Path(BuildConfig.DATASTORE_DIR, name).inSystem
}