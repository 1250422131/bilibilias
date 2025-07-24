package com.imcys.bilibilias.logic.root

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ComponentContextFactory
import com.arkivanov.decompose.GenericComponentContext
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.arkivanov.essenty.instancekeeper.InstanceKeeperOwner
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.subscribe
import com.arkivanov.essenty.statekeeper.StateKeeperOwner
import com.imcys.bilibilias.core.coroutines.AsDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

interface AppComponentContext : GenericComponentContext<AppComponentContext> {
    val logger: Logger
    val backgroundScope: CoroutineScope
    fun init()
}

class DefaultAppComponentContext(
    componentContext: ComponentContext,
) : AppComponentContext,
    LifecycleOwner by componentContext,
    StateKeeperOwner by componentContext,
    InstanceKeeperOwner by componentContext,
    BackHandlerOwner by componentContext {

    override val logger: Logger = Logger.withTag(this::class.simpleName!!)
    override val backgroundScope: CoroutineScope = AsDispatchers.applicationScope

    init {
        lifecycle.subscribe(
            onCreate = ::init,
            onDestroy = {
                backgroundScope.cancel()
            }
        )
    }

    override fun init() {}

    override val componentContextFactory: ComponentContextFactory<AppComponentContext> =
        ComponentContextFactory { lifecycle, stateKeeper, instanceKeeper, backHandler ->
            val ctx = componentContext.componentContextFactory(
                lifecycle,
                stateKeeper,
                instanceKeeper,
                backHandler
            )
            DefaultAppComponentContext(ctx)
        }
}