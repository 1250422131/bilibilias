package com.imcys.bilibilias.core.storage

import com.eygraber.uri.Uri

interface MediaStoreAccess {
    fun createVideo(
        displayName: String,
        mimeType: String,
        relativePath: String,
    ): Uri?

    fun createMediaFile(
        uri: Uri,
        displayName: String,
        mediaType: String,
        relativePath: String
    ): Uri?
}