package com.imcys.bilibilias.database.converter.download

import androidx.room.TypeConverter
import com.imcys.bilibilias.database.entity.download.DownloadTaskNodeType

class DownloadTaskNodeTypeConverter {
    @TypeConverter
    fun fromString(value: String?): DownloadTaskNodeType? {
        return value?.let { DownloadTaskNodeType.valueOf(value) }
    }

    @TypeConverter
    fun stringToDownloadTaskNodeType(type: DownloadTaskNodeType?): String? {
        return type?.name
    }
}