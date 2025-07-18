package com.imcys.bilibilias.ui.login

actual suspend fun savePainterToGallery(
    kmpContext: com.imcys.bilibilias.core.context.KmpContext,
    painter: androidx.compose.ui.graphics.painter.Painter,
    displayName: String,
    mimeType: String,
    quality: Int,
    size: androidx.compose.ui.unit.IntSize
): String? {
    TODO("Not yet implemented")
}