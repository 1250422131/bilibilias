package com.imcys.bilibilias.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.imcys.bilibilias.database.entity.BILIUserCookiesEntity

@Dao
interface BILIUserCookiesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBILIUserCookie(biliUserCookiesEntity: BILIUserCookiesEntity): Long

    @Query("select * from bili_user_cookies where userId = :userId")
    suspend fun getBILIUserCookiesByUid(userId: Long): List<BILIUserCookiesEntity>

    @Query("delete from bili_user_cookies WHERE userId = :userId")
    suspend fun deleteBILICookiesByUid(userId: Long): Int
}