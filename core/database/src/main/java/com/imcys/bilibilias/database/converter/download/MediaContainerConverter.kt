package com.imcys.bilibilias.database.converter.download

import androidx.room.TypeConverter
import com.imcys.bilibilias.database.entity.download.MediaContainer

class MediaContainerConverter {
    @TypeConverter
    fun fromContainer(container: MediaContainer): String =
        container.extension

    @TypeConverter
    fun toContainer(extension: String): MediaContainer =
        MediaContainer.entries.first { it.extension == extension }
}