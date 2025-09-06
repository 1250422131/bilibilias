package com.imcys.bilibilias

import com.imcys.bilibilias.core.navigation.AsBackStack
import com.imcys.bilibilias.navigation.TopLevelDestination
import org.koin.dsl.module

val NavigationModule = module {
    single { AsBackStack(TopLevelDestination.SEARCH.key) }
}