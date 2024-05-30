package com.imcys.bilibilias.feature.player

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import com.imcys.bilibilias.feature.player.component.PlayerComponent
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager

@Composable
fun PlayerContent(component: PlayerComponent) {
    val model by component.models.collectAsStateWithLifecycle()
    PlayerScreen(model)
}

@OptIn(UnstableApi::class)
@Composable
private fun PlayerScreen(model: PlayerComponent.Model, modifier: Modifier = Modifier) {
    AndroidView(factory = {
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
        DanmakuVideoPlayer(it)
    }) {
        it.setMediaSource(model.uris)
    }
}
