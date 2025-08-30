package com.imcys.bilibilias.core.storage

import com.eygraber.uri.Uri
import com.imcys.bilibilias.core.context.KmpContext

actual object AsMediaStore {
    actual fun createVideo(
        context: KmpContext,
        displayName: String,
        mediaType: String,
        relativePath: String
    ): Uri? {
        TODO("Not yet implemented")
    }

    actual fun createMedia(
        context: KmpContext,
        uri: Uri,
        displayName: String,
        mediaType: String,
        relativePath: String
    ): Uri? {
        TODO("Not yet implemented")
    }
}