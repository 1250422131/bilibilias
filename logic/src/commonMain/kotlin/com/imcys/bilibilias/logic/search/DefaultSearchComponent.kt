package com.imcys.bilibilias.logic.search

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class DefaultSearchComponent(
    componentContext: ComponentContext
) : SearchComponent, ComponentContext by componentContext {
    private val viewModelScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
}