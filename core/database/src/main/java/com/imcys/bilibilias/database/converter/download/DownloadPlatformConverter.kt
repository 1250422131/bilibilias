package com.imcys.bilibilias.database.converter.download

import androidx.room.TypeConverter
import com.imcys.bilibilias.database.entity.download.DownloadPlatform

class DownloadPlatformConverter {
    @TypeConverter
    fun fromString(value: String?): DownloadPlatform? {
        return value?.let { DownloadPlatform.valueOf(value) }
    }

    @TypeConverter
    fun stringToDownloadPlatform(downloadPlatform: DownloadPlatform?): String? {
        return downloadPlatform?.name
    }
}