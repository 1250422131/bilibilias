package com.imcys.bilibilias.feature.player

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.media3.common.C
import com.imcys.bilibilias.feature.player.extensions.isSchemaContent
import com.imcys.bilibilias.feature.player.model.VideoState
import com.imcys.bilibilias.feature.player.model.VideoZoom
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.anilbeesetti.nextplayer.core.data.models.VideoState
import dev.anilbeesetti.nextplayer.core.model.VideoZoom
import dev.anilbeesetti.nextplayer.feature.player.extensions.isSchemaContent
import javax.inject.Inject

private const val END_POSITION_OFFSET = 5L

@HiltViewModel
class PlayerViewModel @Inject constructor() : ViewModel() {

    var currentPlaybackPosition: Long? = null
    var currentPlaybackSpeed: Float = 1f
    var currentAudioTrackIndex: Int? = null
    var currentSubtitleTrackIndex: Int? = null
    var currentVideoScale: Float = 1f
    var isPlaybackSpeedChanged: Boolean = false
    val externalSubtitles = mutableSetOf<Uri>()
    var skipSilenceEnabled: Boolean = false

    private var currentVideoState: VideoState? = null

    suspend fun initMediaState(uri: String?) {
        if (currentPlaybackPosition != null) return
        currentVideoState = uri?.let { mediaRepository.getVideoState(it) }
        val prefs = playerPrefs.value

        currentPlaybackPosition = currentVideoState?.position.takeIf { true } ?: currentPlaybackPosition
        currentAudioTrackIndex = currentVideoState?.audioTrackIndex.takeIf { prefs.rememberSelections } ?: currentAudioTrackIndex
        currentSubtitleTrackIndex = currentVideoState?.subtitleTrackIndex.takeIf { prefs.rememberSelections } ?: currentSubtitleTrackIndex
        currentPlaybackSpeed = currentVideoState?.playbackSpeed.takeIf { prefs.rememberSelections } ?: prefs.defaultPlaybackSpeed
        currentVideoScale = currentVideoState?.videoScale.takeIf { prefs.rememberSelections } ?: 1f
        externalSubtitles += currentVideoState?.externalSubs ?: emptyList()
    }

    suspend fun getPlaylistFromUri(uri: Uri): List<Uri> = getSortedPlaylistUseCase.invoke(uri)

    fun saveState(
        uri: Uri,
        position: Long,
        duration: Long,
        audioTrackIndex: Int,
        subtitleTrackIndex: Int,
        playbackSpeed: Float,
        skipSilence: Boolean,
        videoScale: Float,
    ) {
        currentPlaybackPosition = position
        currentAudioTrackIndex = audioTrackIndex
        currentSubtitleTrackIndex = subtitleTrackIndex
        currentPlaybackSpeed = playbackSpeed
        currentVideoScale = videoScale
        skipSilenceEnabled = skipSilence

        if (!uri.isSchemaContent) return

        val newPosition = position.takeIf {
            position < duration - END_POSITION_OFFSET
        } ?: C.TIME_UNSET
    }

    fun setPlayerBrightness(value: Float) {
    }

    fun setVideoZoom(videoZoom: VideoZoom) {
    }

    fun resetAllToDefaults() {
        currentPlaybackPosition = null
        currentPlaybackSpeed = 1f
        currentAudioTrackIndex = null
        currentSubtitleTrackIndex = null
        isPlaybackSpeedChanged = false
        currentVideoScale = 1f
        skipSilenceEnabled = false
        externalSubtitles.clear()
    }
}
