package com.imcys.bilibilias.core.context

import android.content.Context
import androidx.startup.Initializer

@Suppress("unused")
class KmpContextInitializer : Initializer<Context> {
    override fun create(context: Context): Context {
        KmpContext.setUp(context)
        return context
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}