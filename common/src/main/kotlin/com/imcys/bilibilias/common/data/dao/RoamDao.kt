package com.imcys.bilibilias.common.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.imcys.bilibilias.common.data.entity.RoamInfo

@Dao
interface RoamDao {

    @Insert
    suspend fun insert(roamInfo: RoamInfo)

    @Insert
    suspend fun insert(roamInfoMutableList: MutableList<RoamInfo>)

    @Insert
    suspend fun insert(vararg roamInfo: RoamInfo)

    @Delete
    suspend fun delete(roamInfo: RoamInfo)

    @Delete
    suspend fun delete(roamInfoMutableList: MutableList<RoamInfo>)

    @Delete
    suspend fun delete(vararg roamInfo: RoamInfo)


    @Query("SELECT * from as_roam_data ORDER BY id DESC")
    suspend fun getByIdOrderList(): MutableList<RoamInfo>

    @Query("SELECT * from as_roam_data WHERE  id =:id  LIMIT 1")
    suspend fun getByIdQuery(id: Int): RoamInfo


    @Query("SELECT * from as_roam_data WHERE name = :roamName LIMIT 1")
    suspend fun isNameExist(roamName: String): RoamInfo?

}