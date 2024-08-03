package com.bilias.crash

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

class AppInfo(val pkgName: String, val versionCode: String) : Info {
    /**
     * SDK版本号
     */
    @ChecksSdkIntAtLeast
    val sdkVersion: Int = Build.VERSION.SDK_INT

    /**
     * Android版本号
     */
    val release: String = Build.VERSION.RELEASE

    /**
     * 手机型号
     */
    val model: String = Build.MODEL

    /**
     * 手机制造商
     */
    val brand: String = Build.MANUFACTURER

    override fun toString(): String {
        return """
             Mobile Model:$model
             Mobile Brand:$brand
             SDK Version:$sdkVersion
             Android Version:$release
             Version Name:$pkgName
             Version Code:$versionCode
             """.trimIndent()
    }
}