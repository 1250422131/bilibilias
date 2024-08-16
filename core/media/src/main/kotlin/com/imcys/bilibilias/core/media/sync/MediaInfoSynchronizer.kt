package com.imcys.bilibilias.core.media.sync

import android.net.Uri

interface MediaInfoSynchronizer {
    suspend fun addMedia(uri: Uri)
}
