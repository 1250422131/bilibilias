package com.imcys.bilibilias.database.converter

import androidx.room.TypeConverter
import com.imcys.bilibilias.database.entity.ASSharedCookieEncoding
import com.imcys.bilibilias.database.entity.LoginPlatform
import java.util.Date

class CookieEncodingConverter {
    @TypeConverter
    fun fromString(value: String?): ASSharedCookieEncoding? {
        return value?.let { ASSharedCookieEncoding.valueOf(value) }
    }

    @TypeConverter
    fun stringToCookieEncoding(cookieEncoding: ASSharedCookieEncoding?): String? {
        return cookieEncoding?.name
    }
}