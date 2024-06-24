package com.imcys.bilibilias.core.download.media

import android.os.Environment
import java.io.File

enum class ImageMediaDirectory(val folderName: String) {
    PICTURES(Environment.DIRECTORY_PICTURES),
    DCIM(Environment.DIRECTORY_DCIM)
}

enum class AudioMediaDirectory(val folderName: String) {
    MUSIC(Environment.DIRECTORY_MUSIC),
    PODCASTS(Environment.DIRECTORY_PODCASTS),
    RINGTONES(Environment.DIRECTORY_RINGTONES),
    ALARMS(Environment.DIRECTORY_ALARMS),
    NOTIFICATIONS(Environment.DIRECTORY_NOTIFICATIONS)
}

enum class VideoMediaDirectory(val folderName: String) {
    MOVIES(Environment.DIRECTORY_MOVIES),
    DCIM(Environment.DIRECTORY_DCIM)
}

enum class PublicDirectory(val folderName: String) {

    DOWNLOADS(Environment.DIRECTORY_DOWNLOADS),

    /**
     * Returns `null` if you have no URI permissions for read and write in Android 10.
     */
    MUSIC(Environment.DIRECTORY_MUSIC),

    /**
     * Returns `null` if you have no URI permissions for read and write in Android 10.
     */
    PODCASTS(Environment.DIRECTORY_PODCASTS),

    /**
     * Returns `null` if you have no URI permissions for read and write in Android 10.
     */
    RINGTONES(Environment.DIRECTORY_RINGTONES),

    /**
     * Returns `null` if you have no URI permissions for read and write in Android 10.
     */
    ALARMS(Environment.DIRECTORY_ALARMS),

    /**
     * Returns `null` if you have no URI permissions for read and write in Android 10.
     */
    NOTIFICATIONS(Environment.DIRECTORY_NOTIFICATIONS),

    /**
     * Returns `null` if you have no URI permissions for read and write in Android 10.
     */
    PICTURES(Environment.DIRECTORY_PICTURES),

    /**
     * Returns `null` if you have no URI permissions for read and write in Android 10.
     */
    MOVIES(Environment.DIRECTORY_MOVIES),

    /**
     * Returns `null` if you have no URI permissions for read and write in Android 10.
     */
    DCIM(Environment.DIRECTORY_DCIM),

    /**
     * Returns `null` if you have no URI permissions for read and write in Android 10.
     * @see DocumentFileCompat.fromPublicFolder
     */
    DOCUMENTS(Environment.DIRECTORY_DOCUMENTS);

    @Suppress("DEPRECATION")
    val file: File
        get() = Environment.getExternalStoragePublicDirectory(folderName)

    val absolutePath: String
        get() = file.absolutePath
}
