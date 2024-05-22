package com.imcys.bilibilias.feature.login

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
            MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL_PRIMARY
            )
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        val resolver = context.contentResolver
        val contentUri = resolver.insert(collection, contentValues)
        try {
            if (contentUri == null) {
                throw IOException("Failed to create new MediaStore record.")
            }
            val stream = resolver.openOutputStream(contentUri)
            if (stream == null) {
                throw IOException("Failed to get output stream.")
            }
            if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)) {
                throw IOException("Failed to save bitmap.")
            }

            contentValues.clear()
            contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
            resolver.update(contentUri, contentValues, null, null)
        } catch (e: FileNotFoundException) {
            Napier.d(e) { "文件未发现" }
        } catch (e: IOException) {
            Napier.d(e) { "二维码文件错误" }
        } finally {
            goToQRScan(context)
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
