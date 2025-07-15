package com.imcys.bilibilias.ui.login

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import com.imcys.bilibilias.core.context.KmpContext
import io.github.alexzhirkevich.qrose.toImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.IOException

private const val TAG = "ImageExport"
actual suspend fun savePainterToGallery(
    kmpContext: KmpContext,
    painter: Painter,
    displayName: String,
    mimeType: String,
    quality: Int,
    size: IntSize
): String? {
    require(size.toSize().isSpecified) { "Size must be specified" }
    val context = kmpContext.get()
    return withContext(Dispatchers.IO) {
        try {
            val bitmap = painter.toImageBitmap(size.width, size.height)
                .asAndroidBitmap()
                .addWhiteBorder(200)
            val imageUri = createImageUri(
                context,
                displayName,
                mimeType,
                "${Environment.DIRECTORY_PICTURES}/bilibilias/"
            )
            if (imageUri == null) {
                Log.e(TAG, "Failed to create MediaStore entry for $displayName")
                return@withContext null
            } else {
                Log.i(TAG, "imageUri: $imageUri")
                saveImage(context, imageUri, bitmap, mimeType, quality)
            }
            imageUri.toString()
        } catch (e: IOException) {
            Log.e(TAG, "IOException while saving image $displayName", e)
            null
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error while saving image $displayName", e)
            null
        }
    }
}

private fun saveImage(
    context: Context,
    contentUri: Uri?,
    bitmap: Bitmap,
    mimeType: String,
    quality: Int
) {
    if (contentUri == null) {
        throw IOException("Failed to create new MediaStore record (contentUri is null).")
    }
    val compressFormat = getCompressFormat(mimeType)
    context.contentResolver.openOutputStream(contentUri, "w")?.use { stream ->
        if (!bitmap.compress(compressFormat, quality, stream)) {
            throw IOException("Failed to save bitmap with format $compressFormat and quality $quality.")
        }
    } ?: throw IOException("Failed to open output stream for $contentUri.")
}

private fun createImageUri(
    context: Context,
    displayName: String,
    mimeType: String,
    relativePath: String
): Uri? {
    val values = ContentValues().apply {
        // 文件名
        put(MediaStore.Files.FileColumns.DISPLAY_NAME, displayName)

        // 资源类型
        put(MediaStore.Files.FileColumns.MIME_TYPE, mimeType)
    }

    // MediaStore 会根据 Uri 自动存储到对应分类目录下, 并在目录下创建 relativePath 文件夹
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        values.put(MediaStore.Files.FileColumns.RELATIVE_PATH, relativePath)
    }
    val createTime = System.currentTimeMillis()
    // 创建时间
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        values.put(MediaStore.Files.FileColumns.DATE_TAKEN, createTime)
    } else {
        values.put(MediaStore.Files.FileColumns.DATE_ADDED, createTime)
    }
    val resolver = context.contentResolver
    var imageUri: Uri? = null
    try {
        imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    } catch (e: Exception) {
        Log.e(TAG, "Error creating MediaStore entry", e)
    }
    return imageUri
}

private fun getCompressFormat(mimeType: String): Bitmap.CompressFormat {
    return when (mimeType.lowercase()) {
        "image/jpeg", "image/jpg" -> Bitmap.CompressFormat.JPEG
        "image/png" -> Bitmap.CompressFormat.PNG
        "image/webp" -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Bitmap.CompressFormat.WEBP_LOSSY // Or WEBP_LOSSLESS or make configurable
        } else {
            Bitmap.CompressFormat.PNG // Fallback for older APIs
        }

        else -> Bitmap.CompressFormat.PNG // Default fallback
    }
}

fun Bitmap.addWhiteBorder(borderSize: Int): Bitmap {
    val bmpWithBorder = createBitmap(
        getWidth() + borderSize * 2,
        getHeight() + borderSize * 2,
        getConfig() ?: Bitmap.Config.ARGB_8888,
    )
    val canvas = Canvas(bmpWithBorder)
    canvas.drawColor(Color.WHITE)
    val target = RectF(
        borderSize.toFloat(),
        borderSize.toFloat(),
        (width + borderSize).toFloat(),
        (height + borderSize).toFloat(),
    )
    canvas.drawBitmap(this, null, target, null)
    return bmpWithBorder
}