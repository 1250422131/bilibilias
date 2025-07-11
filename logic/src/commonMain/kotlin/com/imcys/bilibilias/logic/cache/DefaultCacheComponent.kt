package com.imcys.bilibilias.logic.cache

import com.arkivanov.decompose.ComponentContext

interface CacheComponent {
}

class DefaultCacheComponent(
    componentContext: ComponentContext
) : CacheComponent, ComponentContext by componentContext {

}
