package com.sockmagic.login

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.net.Uri
import android.os.Environment
import com.hjq.toast.Toaster
import com.imcys.bilibilias.core.common.utils.updatePhotoMedias
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

object QRUtil {

    fun saveQRCode(bitmap: Bitmap, context: Context) {
        val bili =
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "bili"
            )
        try {
            val photo = File(bili, "BILIBILIAS-QR-Code.png")
            photo.outputStream().use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            updatePhotoMedias(context, photo)
        } catch (_: FileNotFoundException) {
        } catch (_: IOException) {
        } finally {
            goToQRScan(context)
        }
    }

    private fun goToQRScan(context: Context) {
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