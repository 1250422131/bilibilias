package com.imcys.bilibilias.ui.login

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.IntSize
import com.imcys.bilibilias.core.context.KmpContext

/**
 * Saves a Composable Painter to the device's image gallery.
 *
 * @param context The Android Context.
 * @param painter The Painter to save.
 * @param displayName The desired display name for the image in the gallery (e.g., "MyQRCode.png").
 * @param mimeType The MIME type of the image (e.g., "image/png" or "image/jpeg").
 * @param quality The quality for image compression (0-100), used for JPEG. Ignored for PNG.
 * @param size The size of the Bitmap to draw the painter onto.
 *                   Ensure this is large enough to capture the painter's details.
 * @return The Uri of the saved image in the MediaStore, or null if saving failed.
 */
expect suspend fun savePainterToGallery(
    kmpContext: KmpContext,
    painter: Painter,
    displayName: String,
    mimeType: String = "image/png",
    quality: Int = 100,
    size: IntSize = IntSize(1024, 1024)
): String?