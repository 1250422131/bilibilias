package com.imcys.bilibilias.common.base.utils.file

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import androidx.core.net.toFile
import androidx.documentfile.provider.DocumentFile


fun Uri.toFilePath(): String {
    return this.toFile().path
}


fun Uri.isUriAuthorized(context: Context): Boolean {
    val persistedUriPermissions = context.contentResolver.persistedUriPermissions
    return persistedUriPermissions.any { it.uri == this }
}

fun DocumentFile.hasSubDirectory( subDirName: String): Boolean {
    val files = this.listFiles()
    for (file in files) {
        if (file.isDirectory && file.name == subDirName) {
            return true
        }
    }
    return false
}
