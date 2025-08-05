package com.imcys.bilibilias.ui.play

import android.opengl.GLSurfaceView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.imcys.bilibilias.render.NativeVideoRenderer
import com.imcys.bilibilias.ui.play.navigation.PlayRoute
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle

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
fun PlayScreen(onToBack: () -> Unit, toRoute: PlayRoute) {

    PlayScaffold(
        onToBack = onToBack
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it.calculateTopPadding())
        ) {
            Text("TODO 来点🐂")
            VideoPlayerScreen()
        }
    }
}

@Composable
private fun VideoPlayerScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        VideoSurface(modifier = Modifier.fillMaxSize())
    }
}

@Composable
private fun VideoSurface(
    modifier: Modifier = Modifier
) {

    val renderer = remember {
        NativeVideoRenderer()
    }

    DisposableEffect(Unit) {
        onDispose(renderer::release)
    }

    AndroidView(
        factory = { ctx ->
            GLSurfaceView(ctx).apply {
                setEGLContextClientVersion(3)
                setRenderer(renderer)
                renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
                preserveEGLContextOnPause = true
            }
        },
        modifier = modifier
    )
}