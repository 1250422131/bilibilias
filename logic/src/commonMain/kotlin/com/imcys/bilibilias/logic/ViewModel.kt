package com.imcys.bilibilias.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

context(viewModel: ViewModel)
fun <T> Flow<T>.stateInViewModelScope(
    initialValue: T,
    started: SharingStarted = SharingStarted.WhileSubscribed(5_000),
): StateFlow<T> = stateIn(viewModel.viewModelScope, started, initialValue)

