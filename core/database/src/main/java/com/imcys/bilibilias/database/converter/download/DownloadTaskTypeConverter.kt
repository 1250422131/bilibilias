package com.imcys.bilibilias.database.converter.download

import androidx.room.TypeConverter
import com.imcys.bilibilias.database.entity.download.DownloadTaskType

class DownloadTaskTypeConverter {
    @TypeConverter
    fun fromString(value: String?): DownloadTaskType? {
        return value?.let { DownloadTaskType.valueOf(value) }
    }

    @TypeConverter
    fun stringToDownloadTaskType(mode: DownloadTaskType?): String? {
        return mode?.name
    }
}