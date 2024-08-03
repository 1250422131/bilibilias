package com.bilias.crash.group

import android.os.Build

/**
 * 按手机的品牌给信息分组
 */
abstract class BrandGroup : Group {
    abstract val brandName: String
    override fun counts(): Boolean = Build.BRAND == brandName
}
