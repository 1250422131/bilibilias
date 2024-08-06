package com.imcys.bilibilias.core.database.util

import android.net.Uri
import androidx.room.TypeConverter

internal class UriConverters {
    @TypeConverter
    fun fromString(value: String): Uri = Uri.parse(value)

    @TypeConverter
    fun toString(uri: Uri): String = uri.toString()
}
