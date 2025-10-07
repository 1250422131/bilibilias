package com.imcys.bilibilias.common.utils

import android.app.usage.StorageStatsManager
import android.os.storage.StorageManager
import android.os.storage.StorageVolume
import android.os.StatFs
import android.content.Context
import android.os.Build
import android.os.Environment.*
import java.io.File
import java.util.*

// 新增数据类，包含总空间、已用空间、可用空间、APP占用空间、BILIBILIAS文件夹占用空间
data class StorageInfoData(
    val totalBytes: Long,
    val usedBytes: Long,
    val availableBytes: Long,
    val appBytes: Long,
    val downloadBytes: Long,
    val cacheTotalBytes: Long // 聚合缓存、video、audio文件夹大小
)

object StorageUtil {
    /**
     * 获取文件夹占用空间（递归统计）
     */
    private fun getFolderSize(file: File?): Long {
        if (file == null || !file.exists()) return 0L
        if (file.isFile) return file.length()
        var size = 0L
        file.listFiles()?.forEach {
            size += getFolderSize(it)
        }
        return size
    }

    /**
     * 获取APP私有目录占用空间（filesDir、cacheDir、codeCacheDir、externalCacheDir、externalFilesDir）
     */
    private fun getAppUsedBytes(context: Context): Long {
        var size = 0L
        val dirs = listOf(
            context.filesDir,
            context.cacheDir,
            context.codeCacheDir,
            context.externalCacheDir,
            context.getExternalFilesDir(null)
        )
        dirs.forEach { size += getFolderSize(it) }
        return size
    }

    /**
     * 获取Download/BILIBILIAS文件夹占用空间
     */
    private fun getDownloadUsedBytes(): Long {
        val bilibiliasDir =
            File(getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS), "BILIBILIAS")
        return getFolderSize(bilibiliasDir)
    }

    /**
     * 统计缓存、video、audio文件夹总大小
     */
    fun getCacheTotalBytes(context: Context): Long {
        val cacheSize = getFolderSize(context.cacheDir) + (context.externalCacheDir?.let { getFolderSize(it) } ?: 0L)
        val videoSize = getFolderSize(context.getExternalFilesDir("video"))
        val audioSize = getFolderSize(context.getExternalFilesDir("audio"))
        return cacheSize + videoSize + audioSize
    }




    /**
     * 返回主存储卷的空间信息，单位 Byte。
     * 如果失败返回所有字段为 -1。
     */
    fun getStorageInfoData(context: Context): StorageInfoData {
        val total: Long
        val avail: Long
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val sm = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
                val stats =
                    context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
                val primaryVolume: StorageVolume = sm.primaryStorageVolume
                val dir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    primaryVolume.directory!!
                } else {
                    getExternalStorageDirectory()
                }
                val uuid: UUID = sm.getUuidForPath(dir)
                total = stats.getTotalBytes(uuid)
                val statFs = StatFs(dir.absolutePath)
                avail = statFs.availableBytes
            } catch (e: Exception) {
                return StorageInfoData(-1L, -1L, -1L, -1L, -1L, -1L)
            }
        } else {
            val path = getExternalStorageDirectory()
            val stat = StatFs(path.absolutePath)
            total = stat.totalBytes
            avail = stat.availableBytes
        }
        val used = total - avail
        val appBytes = getAppUsedBytes(context)
        val downloadBytes = getDownloadUsedBytes()
        val cacheTotalBytes = getCacheTotalBytes(context)
        return StorageInfoData(total, used, avail, appBytes, downloadBytes, cacheTotalBytes)
    }

    /**
     * 统一入口：返回 GB 为单位的字符串，方便调试。
     */
    fun getStorageInfo(context: Context): String {
        val info = getStorageInfoData(context)
        return String.format(
            Locale.getDefault(),
            "总空间：%.2f GB，已用：%.2f GB，可用：%.2f GB，APP占用：%.2f GB，BILIBILIAS占用：%.2f GB",
            info.totalBytes / 1_073_741_824.0,
            info.usedBytes / 1_073_741_824.0,
            info.availableBytes / 1_073_741_824.0,
            info.appBytes / 1_073_741_824.0,
        )
    }

    /**
     * 将字节数转换为合适的单位（MB、GB、TB），返回最小满足的单位字符串（大写）。
     * 例如：不满1GB则返回多少MB，不满1MB则返回多少KB。
     */
    fun formatSize(bytes: Long): String {
        val KB = 1024L
        val MB = KB * 1024
        val GB = MB * 1024
        val TB = GB * 1024
        return when {
            bytes >= TB -> String.format("%.2f TB", bytes / TB.toDouble())
            bytes >= GB -> String.format("%.2f GB", bytes / GB.toDouble())
            bytes >= MB -> String.format("%.2f MB", bytes / MB.toDouble())
            bytes >= KB -> String.format("%.2f KB", bytes / KB.toDouble())
            else -> "$bytes B"
        }
    }

    /**
     * 递归删除文件夹及其所有内容
     */
    private fun deleteFolderRecursively(file: File?): Boolean {
        if (file == null || !file.exists()) return true
        if (file.isFile) return file.delete()
        var success = true
        file.listFiles()?.forEach {
            success = success && deleteFolderRecursively(it)
        }
        return file.delete() && success
    }

    /**
     * 清空缓存、video、audio文件夹下所有内容
     * 返回是否全部成功
     */
    fun clearCache(context: Context): Boolean {
        val cacheDirs = listOf(
            context.cacheDir,
            context.externalCacheDir,
            context.getExternalFilesDir("video"),
            context.getExternalFilesDir("audio")
        )
        var allSuccess = true
        cacheDirs.forEach { dir ->
            allSuccess = allSuccess && deleteFolderRecursively(dir)
            // 删除后重新创建文件夹，避免影响后续缓存写入
            if (dir != null && !dir.exists()) dir.mkdirs()
        }
        return allSuccess
    }
}