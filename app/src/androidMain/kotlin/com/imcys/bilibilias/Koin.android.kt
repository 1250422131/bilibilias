package com.imcys.bilibilias

import androidx.activity.ComponentActivity
import com.arkivanov.decompose.defaultComponentContext
import com.imcys.bilibilias.core.context.KmpContext
import com.imcys.bilibilias.logic.root.AppComponentContext
import com.imcys.bilibilias.logic.root.DefaultAppComponentContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val PlatformModule: Module = module {
    single<AppComponentContext> {
        DefaultAppComponentContext(
            get<ComponentActivity>().defaultComponentContext(),
            KmpContext,
            get()
        )
    }
}