package com.imcys.bilibilias.database.converter.download

import androidx.room.TypeConverter
import com.imcys.bilibilias.database.entity.download.DownloadMode

class DownloadModeConverter {
    @TypeConverter
    fun fromString(value: String?): DownloadMode? {
        return value?.let { DownloadMode.valueOf(value) }
    }

    @TypeConverter
    fun stringToDownloadMode(mode: DownloadMode?): String? {
        return mode?.name
    }
}