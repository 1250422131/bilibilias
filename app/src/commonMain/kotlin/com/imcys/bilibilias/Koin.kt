package com.imcys.bilibilias

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        if (config != null) {
            includes(config)
        } else {
            printLogger()
        }
        modules()
    }
}