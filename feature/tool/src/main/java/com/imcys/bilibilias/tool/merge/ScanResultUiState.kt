package com.imcys.bilibilias.tool.merge

import com.imcys.model.download.Entry
import kotlinx.collections.immutable.ImmutableList

sealed interface ScanResultUiState {
    data object Loading : ScanResultUiState
    data object Empty : ScanResultUiState
    data class Success(val entries: ImmutableList<Entry>) : ScanResultUiState
}
