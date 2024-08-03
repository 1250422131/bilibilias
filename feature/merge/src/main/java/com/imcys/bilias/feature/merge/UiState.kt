package com.imcys.bilias.feature.merge

import com.imcys.model.download.Entry
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class UiState(
    val startMix: Boolean = false,
    val complete: Boolean = false,
    val current: String = "",
    val progress: Int = 0,
    val errorMessage: String = "",
    val mixMessage: String = "",
    val entries: ImmutableList<Entry> = persistentListOf()
)
