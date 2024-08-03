package com.imbys.bilibilias.feature.authorspace

import com.imcys.bilibilias.core.model.video.Bvid

sealed interface AuthorSpaceEvent {
    data object DownloadAll : AuthorSpaceEvent
    data class ChangeSelection(val id: Bvid) : AuthorSpaceEvent
}