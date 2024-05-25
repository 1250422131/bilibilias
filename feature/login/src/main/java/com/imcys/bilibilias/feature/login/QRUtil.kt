package com.imcys.bilibilias.feature.login

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.contentValuesOf
import com.hjq.toast.Toaster
import io.github.aakira.napier.Napier
import java.io.FileNotFoundException
import java.io.IOException

internal object QRUtil {

    fun saveQRCode(bitmap: Bitmap, context: Context) {
        val uri = getExistingImageUriOrNull(context)
        val resolver = context.contentResolver
        if (uri == null) {
            val millis = System.currentTimeMillis()
            val contentValues = contentValuesOf(
                MediaStore.Images.Media.TITLE to "BILIBILIAS-QR-Code",
                MediaStore.Images.Media.DISPLAY_NAME to "BILIBILIAS-QR-Code.png",
                MediaStore.Images.Media.DESCRIPTION to "BILIBILIAS-QR-Code",
                MediaStore.Images.Media.MIME_TYPE to "image/png",
                MediaStore.Images.Media.RELATIVE_PATH to "${Environment.DIRECTORY_PICTURES}/bili",
                MediaStore.Images.Media.DATE_ADDED to millis / 1000L,
                MediaStore.Images.Media.DATE_MODIFIED to millis / 1000L,
                MediaStore.Images.Media.DATE_TAKEN to millis,
            )
            val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
            val contentUri = resolver.insert(collection, contentValues)
            saveImage(context, contentUri, bitmap)
        } else {
            saveImage(context, uri, bitmap)
        }
    }

    fun goToQRScan(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("bilibili://qrscan"))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val packageManager = context.packageManager
        val componentName = intent.resolveActivity(packageManager)
        if (componentName != null) {
            context.startActivity(intent)
        } else {
            Toaster.show("呜哇，您好像没有安装Bilibili")
        }
    }

    private fun saveImage(context: Context, contentUri: Uri?, bitmap: Bitmap) {
        try {
            if (contentUri == null) {
                throw IOException("Failed to create new MediaStore record.")
            }
            context.contentResolver.openOutputStream(contentUri, "w")?.use { stream ->
                if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)) {
                    throw IOException("Failed to save bitmap.")
                }
            }
        } catch (e: FileNotFoundException) {
            Napier.w(e) { "文件未发现" }
        } catch (e: IOException) {
            Napier.w(e) { "二维码文件错误" }
        } finally {
            goToQRScan(context)
        }
    }

    private fun getExistingImageUriOrNull(context: Context): Uri? {
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        val projection = arrayOf(MediaStore.MediaColumns._ID)
        val selection = "${MediaStore.Images.Media.RELATIVE_PATH} = ? AND " +
            "${MediaStore.MediaColumns.DISPLAY_NAME} = ? "
        val selectionArgs = arrayOf("Pictures/bili/", "BILIBILIAS-QR-Code.png")

        context.contentResolver.query(
            collection,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                return ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            }
        }
        return null
    }
}

fun Bitmap.addWhiteBorder(borderSize: Int): Bitmap {
    val bmpWithBorder = Bitmap.createBitmap(
        getWidth() + borderSize * 2,
        getHeight() + borderSize * 2,
        getConfig()
    )
    val canvas = Canvas(bmpWithBorder)
    canvas.drawColor(Color.WHITE)
    val target = RectF(
        borderSize.toFloat(),
        borderSize.toFloat(),
        (width + borderSize).toFloat(),
        (height + borderSize).toFloat()
    )
    canvas.drawBitmap(this, null, target, null)
    return bmpWithBorder
}
