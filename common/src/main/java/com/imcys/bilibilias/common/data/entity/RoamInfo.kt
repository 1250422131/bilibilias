package com.imcys.bilibilias.common.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "as_roam_data")
data class RoamInfo(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "name")
    var romaName: String = "",

    //漫游地址
    @ColumnInfo(name = "path")
    var romaPath: String = "",

    @ColumnInfo(name = "remark")
    var romaRemark: String = "",

    @ColumnInfo(name = "cookie_state")
    var cookieState: Boolean = true,

    //添加日期
    @ColumnInfo(name = "add_date")
    var addTime: Long = System.currentTimeMillis(),

    @Ignore
    var selectState: Int = 0,
    @Ignore
    var checked: Boolean = false,
) {
    constructor() : this(0, "", "", "", true, System.currentTimeMillis(), 0, false)
}
