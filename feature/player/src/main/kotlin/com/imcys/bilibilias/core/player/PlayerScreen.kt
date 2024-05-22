package com.imcys.bilibilias.core.player

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.util.UnstableApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import kotlin.reflect.typeOf

class PlayerScreen(
    private val vUri: Uri,
    private val aUri: Uri,
)  {
    @Composable
    fun Content() {
//        val viewModel: PlayerViewModel = getViewModel()
//        PlayerContent(viewModel.player)
    }
}

@OptIn(UnstableApi::class)
@Composable
fun PlayerContent(player: AsVideoPlayer) {
    Scaffold {
        Column(Modifier.padding(it)) {
            AndroidView(
                factory = { player.playerView },
                onReset = {},
                onRelease = {},
                update = {},
                modifier = Modifier.height(200.dp)
            )
        }
        val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, e ->
                when (e) {
                    Lifecycle.Event.ON_RESUME -> player.playerView.onResume()
                    Lifecycle.Event.ON_PAUSE -> player.playerView.onPause()
                    Lifecycle.Event.ON_DESTROY -> player.onDestroy()
                    Lifecycle.Event.ON_START,
                    Lifecycle.Event.ON_CREATE,
                    Lifecycle.Event.ON_STOP,
                    Lifecycle.Event.ON_ANY -> Unit
                }
            }

            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }
}
