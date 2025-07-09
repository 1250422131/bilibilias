package com.imcys.bilibilias.ui.weight

import androidx.compose.foundation.background
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import coil3.SingletonImageLoader
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter.Companion.DefaultTransform
import coil3.compose.AsyncImagePainter.State
import coil3.compose.LocalPlatformContext

@Composable
fun ASAsyncImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    transform: (State) -> State = DefaultTransform,
    onState: ((State) -> Unit)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DefaultFilterQuality,
    clipToBounds: Boolean = true,
    onClick: () -> Unit = {},
    ) {
    if (!LocalInspectionMode.current) {
        Surface(
            shape = shape,
            color = MaterialTheme.colorScheme.primaryContainer,
            onClick = onClick
        ) {
            AsyncImage(
                model = model,
                contentDescription = contentDescription,
                imageLoader = SingletonImageLoader.get(LocalPlatformContext.current),
                modifier = modifier,
                transform = transform,
                onState = onState,
                alignment = alignment,
                contentScale = contentScale,
                alpha = alpha,
                colorFilter = colorFilter,
                filterQuality = filterQuality,
                clipToBounds = clipToBounds,
            )
        }
    } else {
        Surface(
            modifier,
            color = MaterialTheme.colorScheme.primary,
            shape = CardDefaults.shape
        ) {
        }
    }
}