package com.imcys.bilibilias.tool.merge

sealed interface MergeUiState {
    data class Start(val size: Int) : MergeUiState
    data class Merging(val progress: Int) : MergeUiState
    data class Error(val errorMsg: String?) : MergeUiState
    data class Success(val muxFileName: String) : MergeUiState
    data object Complete : MergeUiState
}
