package com.imcys.bilibilias

import com.imcys.bilibilias.logic.di.logicModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        if (config != null) {
            includes(config)
        } else {
            printLogger()
        }
        modules(appModule, logicModule)
    }
}

expect val appModule: Module