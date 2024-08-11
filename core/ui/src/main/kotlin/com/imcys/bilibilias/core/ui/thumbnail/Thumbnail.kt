package com.imcys.bilibilias.core.ui.thumbnail

import android.net.Uri
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.videoFrameMillis

@Composable
fun Thumbnail(data: Uri, videoFrameMillis: Long, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(data)
            .videoFrameMillis(videoFrameMillis)
            .build(),
        contentScale = ContentScale.Crop,
        contentDescription = "Video Thumbnail",
        modifier = modifier
            .size(80.dp)
            .clip(RoundedCornerShape(12.dp)),
    )
}
