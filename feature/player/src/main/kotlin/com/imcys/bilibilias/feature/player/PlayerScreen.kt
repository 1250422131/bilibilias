package com.imcys.bilibilias.feature.player

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager

@Composable
fun PlayerContent(component: PlayerComponent) {
}

@OptIn(UnstableApi::class)
@Composable
private fun PlayerScreen(modifier: Modifier = Modifier) {
    AndroidView(factory = {
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
        DanmakuVideoPlayer(it)
    }) {
    }
}
