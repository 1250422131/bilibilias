package com.imcys.bilibilias.okdownloader.android.internal

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "download_record")
internal data class RecordEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val id: Long = 0,
    @ColumnInfo(name = "_url")
    val url: String,
    @ColumnInfo(name = "_path")
    val path: String,
    @ColumnInfo(name = "_md5")
    val md5: String,
    @ColumnInfo(name = "_flag")
    val flag: Int = 0
)

@Dao
internal interface RecordDao {

    @Query("select * from download_record where _url = :url")
    fun queryByUrl(url: String): RecordEntity?

    @Insert
    fun insert(entity: RecordEntity): Long

    @Query("delete from download_record where _id <= :id")
    fun deleteLessThen(id: Long): Int

    @Query("select max(_id) from download_record")
    fun getMaxId(): Long
}
