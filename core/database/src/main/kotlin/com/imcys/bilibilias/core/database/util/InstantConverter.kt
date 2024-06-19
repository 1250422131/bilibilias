package com.imcys.bilibilias.core.database.util

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

internal class InstantConverter {
    @TypeConverter
    fun longToInstant(value: Long): Instant =
        value.let(Instant::fromEpochMilliseconds)

    @TypeConverter
    fun instantToLong(instant: Instant): Long =
        instant.toEpochMilliseconds()
}
