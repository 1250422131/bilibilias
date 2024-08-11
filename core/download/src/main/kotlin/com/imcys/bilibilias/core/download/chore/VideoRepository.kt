package com.imcys.bilibilias.core.download.chore

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val PROJECTION = arrayOf(MediaStore.Video.Media._ID)
private const val QUERY = MediaStore.Video.Media.DISPLAY_NAME + " = ?"

class VideoRepository(private val context: Context) {
    private val collection =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Video.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL,
            )
        } else {
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        }

    suspend fun getLocalUri(filename: String): Uri? =
        withContext(Dispatchers.IO) {
            val resolver = context.contentResolver

            resolver.query(collection, PROJECTION, QUERY, arrayOf(filename), null)
                ?.use { cursor ->
                    if (cursor.count > 0) {
                        cursor.moveToFirst()
                        return@withContext ContentUris.withAppendedId(
                            collection,
                            cursor.getLong(0),
                        )
                    }
                }

            null
        }
}
