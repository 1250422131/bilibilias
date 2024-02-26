package com.imcys.bilibilias.common.base.utils.file

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import androidx.documentfile.provider.DocumentFile

fun Uri.toFilePath(): String {
    return this.toFile().path
}


fun Uri.isUriAuthorized(context: Context): Boolean {
    val persistedUriPermissions = context.contentResolver.persistedUriPermissions
    return persistedUriPermissions.any { it.uri == this }
}

fun hasSubDirectory(parentDir: DocumentFile, subDirName: String): Boolean {
    val files = parentDir.listFiles()
    for (file in files) {
        if (file.isDirectory && file.name == subDirName) {
            return true
        }
    }
    return false
}
