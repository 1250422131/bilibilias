package com.imcys.bilibilias.logic.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.imcys.bilibilias.logic.cache.CacheComponent
import com.imcys.bilibilias.logic.search.SearchComponent

interface RootComponent : BackHandlerOwner {

    val stack: Value<ChildStack<*, Child>>
    fun onSearchClicked()
    fun onCacheClicked()
    fun onBackClicked()

    sealed class Child {
        data class SearchChild(val component: SearchComponent) : Child()
        data class CacheChild(val component: CacheComponent) : Child()
    }
}