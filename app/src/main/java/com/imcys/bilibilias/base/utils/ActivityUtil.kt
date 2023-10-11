package com.imcys.bilibilias.base.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.imcys.bilibilias.base.app.App
import timber.log.Timber
import java.io.File

fun Context.startActivity(clazz: Class<out Activity>, bundle: Bundle = Bundle()) {
    val intent = Intent(this, clazz).apply {
        putExtras(bundle)
    }
    startActivity(intent)
}

fun Context.openUri(uri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW, uri)
    // 如果不支持的 Uri 将抛出异常
    startActivity(intent)
}

tailrec fun Context.getActivity(): Activity = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> error("Permissions should be called in the context of an Activity")
}

@RequiresApi(Build.VERSION_CODES.M)
fun Activity.shouldShowRationale(name: String): Boolean {
    return shouldShowRequestPermissionRationale(name)
}

fun Context.hasPickMediaPermission(permission: String = Manifest.permission.WRITE_EXTERNAL_STORAGE): Boolean {
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> true
        else -> isPermissionGranted(name = permission)
    }
}

fun Context.gotoApplicationSettings() {
    startActivity(
        Intent().apply {
            action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.parse("package:$packageName")
        }
    )
}

// 更新图库
fun updatePhotoMedias(vararg files: File) {
    files.forEach {
        MediaScannerConnection.scanFile(App.context, arrayOf(it.path), null) { path, uri ->
            Timber.d("文件路径=$path")
        }
    }
}

fun PackageManager.getPackageInfoCompat(packageName: String, flags: Int = 0): PackageInfo? = try {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
    } else {
        getPackageInfo(packageName, flags)
    }
} catch (e: PackageManager.NameNotFoundException) {
    null
}

fun Context.isPermissionGranted(name: String): Boolean {
    return ContextCompat.checkSelfPermission(
        this, name
    ) == PackageManager.PERMISSION_GRANTED
}