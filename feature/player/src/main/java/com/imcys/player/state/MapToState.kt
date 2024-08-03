package com.imcys.player.state

import com.imcys.model.Dash
import com.imcys.model.NetworkPlayerPlayUrl
import com.imcys.model.PgcPlayUrl
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.plus
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

internal fun NetworkPlayerPlayUrl.mapToPlayerUiState(): PlayerUiState.Success {
    return PlayerUiState.Success(
        video = dash.videoMap().toImmutableMap(),
        audio = dash.audioMap().toImmutableMap(),
        duration = dash.duration,
        descriptionWithQuality = acceptQuality.zip(acceptDescription).toMap().toImmutableMap(),
        supportFormats = supportFormats.associate { supportFormat ->
            supportFormat.quality to SupportFormats(
                supportFormat.quality, supportFormat.newDescription,
                supportFormat.codecs.any { it.startsWith("av01") },
                supportFormat.codecs.any { it.startsWith("avc1") },
                supportFormat.codecs.any { it.startsWith("hev1") },
            )
        }.toImmutableMap(),
        defaultQuality = quality
    )
}

internal fun PgcPlayUrl.mapToPlayerUiState(): PlayerUiState.Success {
    val codecId = videoInfo.videoCodecid
    var result = persistentMapOf<Quality, ImmutableList<PlayLinkInfo>>()
    var resultLink = persistentListOf<PlayLinkInfo>()
    for (d in videoInfo.durls) {
        val id = d.quality
        for (durl in d.durl) {
            resultLink += PlayLinkInfo(
                id = id,
                baseUrl = durl.url,
                backupUrl = durl.backupUrl.toImmutableList(),
                codecId = codecId,
                isAVC = codecId == 7, isHEVC = codecId == 12, isAV1 = codecId == 13,
                type = "bangumi"
            )
        }
        result += id to resultLink
    }

    return PlayerUiState.Success(
        video = result,
        audio = persistentMapOf(),
        duration = videoInfo.timelength,
        descriptionWithQuality = videoInfo.acceptQuality.zip(videoInfo.acceptDescription).toMap()
            .toImmutableMap(),
        supportFormats = videoInfo.supportFormats.associate {
            it.quality to SupportFormats(
                it.quality,
                it.newDescription,
                supportAV1 = false, supportAVC1 = false, supportHEV1 = false
            )
        }.toImmutableMap(),
        defaultQuality = videoInfo.quality
    )
}

private fun Dash.videoMap(): Map<Quality, ImmutableList<PlayLinkInfo>> {
    return video.groupBy { it.id }.mapValues { entry ->
        entry.value.map {
            it.mapToPlayLinkInfo()
        }.toImmutableList()
    }
}

private fun Dash.Video.mapToPlayLinkInfo(): PlayLinkInfo {
    return PlayLinkInfo(
        id = id,
        baseUrl = baseUrl,
        backupUrl = backupUrl.toImmutableList(),
        bandwidth = bandwidth,
        mimeType = mimeType,
        codecs = codecs,
        codecId = codecid,
        width = width,
        height = height,
        type = "video", isAVC = codecid == 7, isHEVC = codecid == 12, isAV1 = codecid == 13
    )
}

private fun Dash.audioMap(): Map<Quality, ImmutableList<PlayLinkInfo>> {
    return audio.groupBy { it.id }.mapValues { entry ->
        entry.value.map {
            it.mapToPlayLinkInfo()
        }.toImmutableList()
    }
}

private fun Dash.Audio.mapToPlayLinkInfo(): PlayLinkInfo {
    return PlayLinkInfo(
        id = id,
        baseUrl = baseUrl,
        backupUrl = backupUrl.toImmutableList(),
        bandwidth = bandwidth,
        mimeType = mimeType,
        codecs = codecs,
        codecId = codecid,
        width = width,
        height = height,
        type = "audio", isAVC = codecid == 7, isHEVC = codecid == 12, isAV1 = codecid == 13
    )
}

