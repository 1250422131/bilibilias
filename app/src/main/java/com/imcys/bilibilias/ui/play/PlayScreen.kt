package com.imcys.bilibilias.ui.play

import android.opengl.GLSurfaceView
import android.view.Surface
import android.view.TextureView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
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
fun PlayScreen(
    onToBack: () -> Unit,
    route: PlayRoute,
) {

    val context = LocalContext.current
    val vm: PlayViewModel = viewModel {
        PlayViewModel()
    }
    val fd = remember {
        getFileDescriptorFromContentUri(context, route.savePath)!!
    }

    val isPlaying by vm.isPlaying.collectAsState()

    PlayScaffold(
        onToBack = onToBack
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it.calculateTopPadding())
        ) {
            Text("TODO 来点🐂")
            VideoPlayerScreen(
                isPlaying = isPlaying,
                onPlay = vm::testPlay,
                onPause = vm::pause,
                onSurfaceReady = { surface, w, h ->
                    vm.initializeRenderer(surface, fd, w, h)
                },
                onSurfaceChanged = vm::updateViewport,
                onSurfaceDestroyed = vm::release
            )
        }
    }
}

@Composable
private fun VideoPlayerScreen(
    isPlaying: Boolean,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onSurfaceReady: (Surface, Int, Int) -> Unit,
    onSurfaceChanged: (Int, Int) -> Unit,
    onSurfaceDestroyed: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .border(1.dp, Color.Red),
        ) {
            VideoSurface(
                onSurfaceReady = onSurfaceReady,
                onSurfaceChanged = onSurfaceChanged,
                onSurfaceDestroyed = onSurfaceDestroyed,
                modifier = Modifier.fillMaxSize()
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { if (isPlaying) onPause() else onPlay() }
            ) {
                Text(if (isPlaying) "Pause" else "Play")
            }
        }
    }

}

@Composable
private fun VideoSurface(
    onSurfaceReady: (Surface, Int, Int) -> Unit,
    onSurfaceChanged: (Int, Int) -> Unit,
    onSurfaceDestroyed: () -> Unit,
    modifier: Modifier = Modifier

) {
    DisposableEffect(Unit) {
        onDispose {
            onSurfaceDestroyed()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            TextureView(ctx).apply {
                surfaceTextureListener = VideoSurfaceTextureListener(
                    onSurfaceReady = onSurfaceReady,
                    onSurfaceChanged = onSurfaceChanged,
                    onSurfaceDestroyed = onSurfaceDestroyed
                )
            }
        }
    )
}
