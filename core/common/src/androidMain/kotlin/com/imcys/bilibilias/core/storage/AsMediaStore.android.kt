package com.imcys.bilibilias.core.storage

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import com.eygraber.uri.Uri
import com.eygraber.uri.toAndroidUri
import com.eygraber.uri.toKmpUri
import com.eygraber.uri.toKmpUriOrNull
import com.imcys.bilibilias.core.context.KmpContext
import android.net.Uri as AndroidUri

actual object AsMediaStore {
    actual fun createVideo(
        context: KmpContext,
        displayName: String,
        mediaType: String,
        relativePath: String,
    ): Uri? {
        return createMedia(
            context,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI.toKmpUri(),
            displayName,
            mediaType,
            relativePath
        )
    }

    actual fun createImage(
        context: KmpContext,
        displayName: String,
        mediaType: String,
        relativePath: String,
    ): Uri? {
        return createMedia(
            context,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toKmpUri(),
            displayName,
            mediaType,
            relativePath
        )
    }

    actual fun createMedia(
        context: KmpContext,
        uri: Uri,
        displayName: String,
        mediaType: String,
        relativePath: String,
    ): Uri? {
        val context = context.get()
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
        val result = try {
            context.contentResolver.insert(uri, contentValues)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        return result?.toKmpUriOrNull()
    }
}