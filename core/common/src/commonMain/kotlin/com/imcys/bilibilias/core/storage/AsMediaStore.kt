package com.imcys.bilibilias.core.storage

import com.eygraber.uri.Uri
import com.imcys.bilibilias.core.context.KmpContext

expect object AsMediaStore {
    fun createVideo(
        context: KmpContext,
        displayName: String,
        mediaType: String,
        relativePath: String,
    ): Uri?

    fun createMedia(
        context: KmpContext,
        uri: Uri,
        displayName: String,
        mediaType: String,
        relativePath: String
    ): Uri?
}