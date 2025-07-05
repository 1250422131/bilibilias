package com.imcys.bilibilias.database.converter

import androidx.room.TypeConverter
import com.imcys.bilibilias.database.entity.LoginPlatform

class LoginPlatformConverter {
    @TypeConverter
    fun fromString(value: String?): LoginPlatform? {
        return value?.let { LoginPlatform.valueOf(value) }
    }

    @TypeConverter
    fun stringToLoginPlatform(loginPlatform: LoginPlatform?): String? {
        return loginPlatform?.name
    }
}