package com.imcys.bilibilias.core.storage

import com.eygraber.uri.Uri

class DesktopMediaStoreAccess : MediaStoreAccess {
    override fun createVideo(
        displayName: String,
        mimeType: String,
        relativePath: String
    ): Uri? {
        TODO("Not yet implemented")
    }

    override fun createMediaFile(
        uri: Uri,
        displayName: String,
        mediaType: String,
        relativePath: String
    ): Uri? {
        TODO("Not yet implemented")
    }
}