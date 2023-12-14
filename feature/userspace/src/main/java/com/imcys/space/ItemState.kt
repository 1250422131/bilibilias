package com.imcys.space

import com.imcys.model.video.ViewDetailAndPlayUrl
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ItemState(
    val title: String = "",
    val codes: String = "",
    val items: ImmutableList<ViewDetailAndPlayUrl> = persistentListOf()
)
