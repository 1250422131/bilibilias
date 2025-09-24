package com.imcys.bilibilias.weight

import android.view.TextureView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun ASTextureView(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val textureView = remember { TextureView(context) }
    AndroidView(factory = { textureView }, modifier = modifier)
}

@Composable
fun ASPlayer(modifier: Modifier = Modifier) {
    ASTextureView(modifier)
}