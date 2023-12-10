package com.bilias.crash

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.bilias.crash.policy.StoragePolicy


object Crash {
    fun initCrash(context: Context, crashInfoFolder: String) {
        val name = getVersionName(context)
        val code = getVersionCode(context)
        val appInfo = AppInfo(name, code)
        CrashConfig.Builder(appInfo)
            .crashReportPolicy(StoragePolicy(folderName = crashInfoFolder)).build()
    }

    private fun getVersionName(context: Context): String {
        val packageManager = context.packageManager
        try {
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return "UNKNOWN"
    }

    private fun getVersionCode(context: Context): String {
        val packageManager = context.packageManager
        try {
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                return packageInfo.longVersionCode.toString()
            }
            return packageInfo.versionCode.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return "UNKNOWN"
    }
}