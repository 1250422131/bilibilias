package com.imcys.bilibilias.logic.data

data class MediaResource(
    val title: String,
    val cover: String,
    val videoPart: MediaPart,
    val audioPart: MediaPart,
    val parts: List<MediaPart>
)

data class MediaPart(
    val id: String,
    val uri: String,
    val kind: MediaSourceKind,
)

enum class MediaSourceKind {
    SUBTITLE,
    VIDEO,
    AUDIO,
    ;
}