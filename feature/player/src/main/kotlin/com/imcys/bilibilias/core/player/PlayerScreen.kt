package com.imcys.bilibilias.core.player

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel

class PlayerScreen(
    private val vUri: Uri,
    private val aUri: Uri,
) : Screen {
    @Composable
    override fun Content() {
        val viewModel: PlayerViewModel = getViewModel()
        PlayerContent(viewModel.player)
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
    }
}
