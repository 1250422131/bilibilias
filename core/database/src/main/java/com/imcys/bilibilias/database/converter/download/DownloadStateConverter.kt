package com.imcys.bilibilias.database.converter.download

import androidx.room.TypeConverter
import com.imcys.bilibilias.database.entity.download.DownloadState

class DownloadStateConverter {
    @TypeConverter
    fun fromString(value: String?): DownloadState? {
        return value?.let { DownloadState.valueOf(value) }
    }

    @TypeConverter
    fun stringToDownloadState(type: DownloadState?): String? {
        return type?.name
    }
}