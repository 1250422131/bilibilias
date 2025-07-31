package com.imcys.bilibilias.logic.root

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ComponentContextFactory
import com.arkivanov.decompose.GenericComponentContext
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.arkivanov.essenty.instancekeeper.InstanceKeeperOwner
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.statekeeper.StateKeeperOwner
import com.imcys.bilibilias.core.context.KmpContext
import com.imcys.bilibilias.core.coroutines.BackgroundScope
import kotlinx.coroutines.CoroutineScope
import org.koin.core.component.KoinComponent

interface AppComponentContext :
    GenericComponentContext<AppComponentContext>,
    BackgroundScope,
    KoinComponent {
    val logTag: String
    val context: KmpContext

    @Deprecated("Use com.imcys.bilibilias.core.logging.logger<Class>()")
    val logger: Logger
    fun init()
}

class DefaultAppComponentContext(
    componentContext: ComponentContext,
    override val context: KmpContext,
    coroutineScope: CoroutineScope,
) : AppComponentContext,
    LifecycleOwner by componentContext,
    StateKeeperOwner by componentContext,
    InstanceKeeperOwner by componentContext,
    BackHandlerOwner by componentContext {
    override val applicationScope = coroutineScope

    override val logTag: String = "AppComponent"

    @Deprecated("Use com.imcys.bilibilias.core.logging.logger<Class>()")
    override val logger: Logger = Logger.withTag(logTag)
    override fun init() {
    }

    override val componentContextFactory: ComponentContextFactory<AppComponentContext> =
        ComponentContextFactory { lifecycle, stateKeeper, instanceKeeper, backHandler ->
            val ctx = componentContext.componentContextFactory(
                lifecycle,
                stateKeeper,
                instanceKeeper,
                backHandler
            )
            DefaultAppComponentContext(ctx, context, applicationScope)
        }
}