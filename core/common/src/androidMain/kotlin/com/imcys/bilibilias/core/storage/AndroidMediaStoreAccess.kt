package com.imcys.bilibilias.core.storage

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import com.eygraber.uri.Uri
import com.eygraber.uri.toAndroidUri
import com.eygraber.uri.toKmpUri
import com.eygraber.uri.toKmpUriOrNull
import com.imcys.bilibilias.core.logging.logger
import java.io.File
import android.net.Uri as AndroidUri

class AndroidMediaStoreAccess(
    private val context: Context,
) : MediaStoreAccess {
    private val logger = logger<AndroidMediaStoreAccess>()
    override fun createVideo(
        displayName: String,
        mimeType: String,
        relativePath: String,
    ): Uri? {
        return createMediaFile(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI.toKmpUri(),
            displayName,
            mimeType,
            "${Environment.DIRECTORY_MOVIES}${File.separator}$relativePath"
        )
    }

    override fun createMediaFile(
        uri: Uri,
        displayName: String,
        mediaType: String,
        relativePath: String,
    ): Uri? {
        val contentValues =
            ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
                put(MediaStore.MediaColumns.MIME_TYPE, mediaType)
                val dateCreated = System.currentTimeMillis()
                put(MediaStore.MediaColumns.DATE_ADDED, dateCreated)
                put(MediaStore.MediaColumns.DATE_MODIFIED, dateCreated)
                put(MediaStore.MediaColumns.OWNER_PACKAGE_NAME, context.packageName)
                if (relativePath.isNotBlank()) {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
                }
            }

        return tryInsertMediaFile(
            context,
            uri.toAndroidUri(),
            contentValues
        )
    }

    private fun tryInsertMediaFile(
        context: Context,
        uri: AndroidUri,
        contentValues: ContentValues
    ): Uri? {
        logger.info { "Inserting media file.Display name: ${contentValues.getAsString(MediaStore.MediaColumns.DISPLAY_NAME)}" }
        val result = try {
            context.contentResolver.insert(uri, contentValues)
        } catch (e: Exception) {
            logger.error(e) { "Failed to insert media file." }
            null
        }
        return result?.toKmpUriOrNull()
    }
}