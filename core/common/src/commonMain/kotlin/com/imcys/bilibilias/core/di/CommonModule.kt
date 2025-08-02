package com.imcys.bilibilias.core.di

import com.imcys.bilibilias.core.context.KmpContext
import org.koin.dsl.module

val CommonModule = module {
    single { KmpContext }
}