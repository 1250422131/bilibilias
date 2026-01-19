package com.imcys.bilibilias.database.entity.download

enum class ContainerAudience {
    VIDEO,
    AUDIO,
    VIDEO_AUDIO,
}


sealed interface MediaContainer {
    val extension: String
    val mimeType: String
    val audience: ContainerAudience

    fun canEmbedCover(): Boolean = false
    fun canEmbedSubtitle(): Boolean = false

    companion object {
        val entries: List<MediaContainer> by lazy {
            listOf(M4A, MP3, MP4, MKV)
        }
    }

    object M4A : MediaContainer {
        override val extension = "m4a"
        override val mimeType = "audio/mp4"
        override val audience = ContainerAudience.AUDIO
        override fun canEmbedCover() = true
    }

    object MP3 : MediaContainer {
        override val extension = "mp3"
        override val mimeType = "audio/mpeg"
        override val audience = ContainerAudience.AUDIO
    }

    object MP4 : MediaContainer {
        override val extension = "mp4"
        override val mimeType = "video/mp4"
        override val audience = ContainerAudience.VIDEO
        override fun canEmbedCover() = true
        override fun canEmbedSubtitle() = true
    }

    object MKV : MediaContainer {
        override val extension = "mkv"
        override val mimeType = "video/x-matroska"
        override val audience = ContainerAudience.VIDEO
        override fun canEmbedCover() = true
        override fun canEmbedSubtitle() = true
    }
}


val audioContainer by lazy {
    MediaContainer.entries.filter { it.audience != ContainerAudience.VIDEO }
}
val videoContainer by lazy {
    MediaContainer.entries.filter { it.audience != ContainerAudience.AUDIO }
}