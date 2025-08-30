package com.imcys.bilibilias.logic.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ComponentContextFactory
import com.arkivanov.decompose.GenericComponentContext
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.arkivanov.essenty.instancekeeper.InstanceKeeperOwner
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.statekeeper.StateKeeperOwner
import com.imcys.bilibilias.core.coroutines.BackgroundScope
import kotlinx.coroutines.CoroutineScope
import org.koin.core.component.KoinComponent

interface AppComponentContext :
    GenericComponentContext<AppComponentContext>,
    BackgroundScope,
    KoinComponent {
    val lifecycleScope: CoroutineScope
}

class DefaultAppComponentContext(
    componentContext: ComponentContext,
    override val applicationScope: CoroutineScope,
) : AppComponentContext,
    LifecycleOwner by componentContext,
    StateKeeperOwner by componentContext,
    InstanceKeeperOwner by componentContext,
    BackHandlerOwner by componentContext {
    override val lifecycleScope: CoroutineScope = coroutineScope()
    override val componentContextFactory: ComponentContextFactory<AppComponentContext> =
        ComponentContextFactory { lifecycle, stateKeeper, instanceKeeper, backHandler ->
            val ctx = componentContext.componentContextFactory(
                lifecycle,
                stateKeeper,
                instanceKeeper,
                backHandler
            )
            DefaultAppComponentContext(ctx, applicationScope)
        }
}