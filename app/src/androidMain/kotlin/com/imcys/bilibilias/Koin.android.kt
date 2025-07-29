package com.imcys.bilibilias

import androidx.activity.ComponentActivity
import com.arkivanov.decompose.defaultComponentContext
import com.imcys.bilibilias.core.context.KmpContext
import com.imcys.bilibilias.core.coroutines.AsDispatchers
import com.imcys.bilibilias.logic.root.AppComponentContext
import com.imcys.bilibilias.logic.root.DefaultAppComponentContext
import com.imcys.bilibilias.logic.root.DefaultRootComponent
import com.imcys.bilibilias.logic.root.RootComponent
import org.koin.core.module.Module
import org.koin.dsl.module

actual val appModule: Module = module {
    scope<ComponentActivity> {
        scoped<RootComponent> {
            DefaultRootComponent(get())
        }
    }
    scope<ComponentActivity> {
        scoped<AppComponentContext> {
            DefaultAppComponentContext(
                get<ComponentActivity>().defaultComponentContext(),
                KmpContext,
                AsDispatchers.applicationScope
            )
        }
    }
}