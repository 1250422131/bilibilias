package com.imcys.bilibilias.common.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

object AppUtils {
    fun getVersion(context: Context): Pair<Long, String> {
        val pm = context.packageManager
        val pkg = context.packageName
        val pi = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.getPackageInfo(pkg, PackageManager.PackageInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION") pm.getPackageInfo(pkg, 0)
        }

        val code = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            pi.longVersionCode
        } else {
            @Suppress("DEPRECATION") pi.versionCode.toLong()
        }
        val name = pi.versionName ?: "unknown"

        return code to name
    }
}