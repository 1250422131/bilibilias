package com.imcys.bilibilias.ui.play

import android.opengl.GLSurfaceView
import android.view.TextureView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.imcys.bilibilias.render.NativeVideoRenderer
import com.imcys.bilibilias.render.VideoSurfaceTextureListener
import com.imcys.bilibilias.render.getFileDescriptorFromContentUri
import com.imcys.bilibilias.ui.play.navigation.PlayRoute
import kotlinx.coroutines.delay

@Composable
private fun PlayScaffold(
    onToBack: () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    // TODO back
    Scaffold{
        content(it)
    }
}

@Composable
fun PlayScreen(onToBack: () -> Unit, route: PlayRoute) {

    PlayScaffold(
        onToBack = onToBack
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it.calculateTopPadding())
        ) {
            Text("TODO 来点🐂")
            VideoPlayerScreen(route.savePath)
        }
    }
}

@Composable
private fun VideoPlayerScreen(
    videoPath: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .border(1.dp, Color.Red),
    ) {
        VideoSurface(videoPath, modifier = Modifier.fillMaxSize())

    }

    Button(onClick = {

    }) {
        Text("Play")
    }

    Button(onClick = {

    }) {
        Text("Pause")
    }
}

@Composable
private fun VideoSurface(
    videoPath: String,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    val textureView = remember { TextureView(context) }

    Column(modifier = modifier) {
        AndroidView(
            factory = { ctx ->
                textureView.apply {
                    surfaceTextureListener = VideoSurfaceTextureListener()
                }
            },
        )

    }
}