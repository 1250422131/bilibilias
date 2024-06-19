package com.imcys.bilibilias.core.database.util

import android.net.Uri
import androidx.room.TypeConverter

internal class UriConverters {
    @TypeConverter
    fun fromString(value: String): Uri {
        return Uri.parse(value)
    }

    @TypeConverter
    fun toString(uri: Uri): String {
        return uri.toString()
    }
}
