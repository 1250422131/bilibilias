package com.imcys.bilibilias.core.player

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.MergingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@UnstableApi
class AsVideoPlayer @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    val playerView = PlayerView(context)
    private val exoPlayer = ExoPlayer.Builder(context)
//        .setMediaSourceFactory(DefaultMediaSourceFactory(dataSourceFactory))
        .setRenderersFactory(DefaultRenderersFactory(context).setEnableDecoderFallback(true))
        .build()

    init {

        playerView.player = exoPlayer
    }

    fun config(playerInstance: ExoPlayer.() -> Unit = {}) {
        playerInstance(exoPlayer)
    }

    fun player() {
        exoPlayer.play()
    }

    fun addMediaSource(source: MediaSource) {
        exoPlayer.addMediaSource(source)
        exoPlayer.prepare()
    }

    fun onDestroy() {
        exoPlayer.release()
    }

    fun mediaSource(vUri: Uri, aUri: Uri): MediaSource {
        val dataSourceFactory = DefaultDataSource.Factory(context)

        val videoMediaSource =
            ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(vUri))

        val audioMediaSource =
            ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(aUri))

        val mergedMediaSource = MergingMediaSource(videoMediaSource, audioMediaSource)
        return mergedMediaSource
    }
}
