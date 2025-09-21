package com.imcys.bilibilias.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.imcys.bilibilias.database.converter.DateConverter
import com.imcys.bilibilias.database.converter.LoginPlatformConverter
import java.util.Date

@Entity(tableName = "bili_users")
@TypeConverters(DateConverter::class, LoginPlatformConverter::class)
data class BILIUsersEntity(
    @PrimaryKey(true)
    var id: Long = 0,
    @ColumnInfo(name = "login_platform")
    val loginPlatform: LoginPlatform,
    @ColumnInfo(name = "mid")
    val mid: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "face")
    val face: String,
    @ColumnInfo("level")
    val level: Int,
    @ColumnInfo("vip_state")
    val vipState: Int,
    @ColumnInfo("refresh_token")
    val refreshToken: String?,
    @ColumnInfo("access_token")
    val accessToken: String?,
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),
) {
    fun isVip(): Boolean {
        return vipState > 0
    }
}