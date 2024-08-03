package com.bilias.crash.group

import android.os.Build

/**
 * 安卓手机版本分组
 */
class AndroidVersionGroup(private val version: String) : Group {
    override val name: String = javaClass.simpleName
    override fun counts(): Boolean = Build.VERSION.RELEASE == version
}