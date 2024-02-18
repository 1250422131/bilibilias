package com.imcys.authentication.login

import android.*
import android.content.*
import android.content.pm.*
import android.graphics.*
import android.graphics.Bitmap.CompressFormat
import android.net.*
import android.os.*
import android.provider.*
import androidx.core.content.*
import io.github.aakira.napier.*
import timber.log.*
import java.io.*

object ImageSaveUtil {
    private const val TAG = "ImageSaveUtil"

    /**
     * 保存图片到公共目录 29 以下，需要提前申请文件读写权限 29及29以上的，不需要权限 保存的文件在 DCIM 目录下
     *
     * @param context 上下文
     * @param bitmap 需要保存的bitmap
     * @param format 图片格式
     * @param quality 压缩的图片质量
     * @param recycle 完成以后，是否回收Bitmap，建议为true
     * @return 文件的 uri
     */
    fun saveAlbum(
        context: Context,
        bitmap: Bitmap,
        format: CompressFormat,
        quality: Int,
        recycle: Boolean
    ): Uri? {
        val suffix = if (CompressFormat.JPEG == format) "JPG" else format.name
        val fileName = System.currentTimeMillis().toString() + "_" + quality + "." + suffix
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (!isGranted(context)) {
                Timber.tag(TAG).e("save to album need storage permission")
                return null
            }
            val picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            val destFile = File(picDir, fileName)
            if (!save(bitmap, destFile, format, quality, recycle)) return null
            var uri: Uri? = null
            if (destFile.exists()) {
                uri = Uri.parse("file://" + destFile.absolutePath)
                val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                intent.setData(uri)
                context.sendBroadcast(intent)
            }
            uri
        } else {
            // Android 10 使用
            val contentUri: Uri =
                if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else {
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI
                }
            val contentValues = contentValuesOf(
                MediaStore.Images.Media.DISPLAY_NAME to fileName,
                MediaStore.Images.Media.MIME_TYPE to "image/*",
                MediaStore.Images.Media.RELATIVE_PATH to Environment.DIRECTORY_DCIM + "/",
                // 告诉系统，文件还未准备好，暂时不对外暴露
                MediaStore.MediaColumns.IS_PENDING to 1
            )
            val uri = context.contentResolver.insert(contentUri, contentValues) ?: return null
            try {
                context.contentResolver.openOutputStream(uri)?.use {
                    bitmap.compress(format, quality, it)
                }
                // 告诉系统，文件准备好了，可以提供给外部了
                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                context.contentResolver.update(uri, contentValues, null, null)
                uri
            } catch (e: Exception) {
                Napier.d(tag = TAG, throwable = e) { "保存至相册失败" }
                // 失败的时候，删除此 uri 记录
                context.contentResolver.delete(uri, null, null)
                null
            }
        }
    }

    private fun save(
        bitmap: Bitmap,
        file: File,
        format: CompressFormat,
        quality: Int,
        recycle: Boolean
    ): Boolean {
        if (isEmptyBitmap(bitmap)) {
            Timber.tag(TAG).e("bitmap is empty.")
            return false
        }
        if (bitmap.isRecycled) {
            Timber.tag(TAG).e("bitmap is recycled.")
            return false
        }
        if (!createFile(file, true)) {
            Timber.tag(TAG).e("%s file> failed.", "create or delete file <" + "$")
            return false
        }
        try {
            BufferedOutputStream(FileOutputStream(file)).use {
                bitmap.compress(format, quality, it)
            }
            if (recycle && !bitmap.isRecycled) bitmap.recycle()
        } catch (e: IOException) {
            e.printStackTrace()
            Napier.d(e, TAG) { "保存失败" }
        }
        return true
    }

    private fun isEmptyBitmap(bitmap: Bitmap?): Boolean {
        return bitmap == null || bitmap.isRecycled || bitmap.getWidth() == 0 || bitmap.getHeight() == 0
    }

    private fun createFile(file: File?, isDeleteOldFile: Boolean): Boolean {
        if (file == null) return false
        if (file.exists()) {
            if (isDeleteOldFile) {
                if (!file.delete()) return false
            } else {
                return file.isFile()
            }
        }
        return if (!createDir(file.getParentFile())) {
            false
        } else {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                false
            }
        }
    }

    private fun createDir(file: File?): Boolean {
        if (file == null) return false
        return if (file.exists()) file.isDirectory() else file.mkdirs()
    }

    private fun isGranted(context: Context): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    // 根据uri获取真实路径
    fun getRealFilePath(context: Context, uri: Uri?): String? {
        if (null == uri) return null
        val scheme = uri.scheme
        var data: String? = null
        if (scheme == null) {
            data = uri.path
        } else if (ContentResolver.SCHEME_FILE == scheme) {
            data = uri.path
        } else if (ContentResolver.SCHEME_CONTENT == scheme) {
            val cursor = context.contentResolver.query(
                uri,
                arrayOf(MediaStore.Images.ImageColumns.DATA),
                null,
                null,
                null
            )
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    if (index > -1) {
                        data = cursor.getString(index)
                    }
                }
                cursor.close()
            }
        }
        return data
    }
}
