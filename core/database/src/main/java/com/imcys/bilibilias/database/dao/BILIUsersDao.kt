package com.imcys.bilibilias.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.imcys.bilibilias.database.entity.BILIUsersEntity
import com.imcys.bilibilias.database.entity.LoginPlatform

@Dao
interface BILIUsersDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBILIUser(biliUsersEntity: BILIUsersEntity): Long

    @Update
    suspend fun updateBILIUser(biliUsersEntity: BILIUsersEntity)

    @Query("select * from bili_users where mid = :mid and login_platform = :loginPlatform")
    suspend fun getBILIUserByMidAndPlatform(
        mid: Long,
        loginPlatform: LoginPlatform
    ): BILIUsersEntity?

    @Query("select * from bili_users where login_platform = :loginPlatform")
    suspend fun getBILIUserByPlatform(
        loginPlatform: LoginPlatform
    ): BILIUsersEntity?

    @Query("select * from bili_users where mid in (select mid from bili_users where id = :uid )")
    suspend fun getBILIUserListByUid(
        uid: Long
    ): List<BILIUsersEntity>


    @Query("select * from bili_users where login_platform = :loginPlatform")
    suspend fun getBILIUserListByPlatform(
        loginPlatform: LoginPlatform
    ): List<BILIUsersEntity>


    @Query("select * from bili_users where id = :uid")
    suspend fun getBILIUserByUid(
        uid: Long
    ): BILIUsersEntity?

    @Query("delete from bili_users WHERE id = :userId")
    suspend fun deleteBILIUserByUid(userId: Long): Int
}