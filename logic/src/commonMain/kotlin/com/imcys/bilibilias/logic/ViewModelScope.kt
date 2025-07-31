package com.imcys.bilibilias.logic

import com.arkivanov.decompose.GenericComponentContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

private const val INSTANCE_KEY = "CoroutineScope.INSTANCE_KEY"

val <T : Any> GenericComponentContext<T>.viewModelScope: CoroutineScope
    get() {
        val scope = instanceKeeper.get(INSTANCE_KEY)
        if (scope is CoroutineScope) return scope

        fun destroy() {
            try {
                scope?.onDestroy()
            } catch (e: Exception) {
                throw RuntimeException(e)
            } finally {
                instanceKeeper.remove(INSTANCE_KEY)
            }
        }
        lifecycle.doOnDestroy(::destroy)

        return DestroyableCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).also {
            instanceKeeper.put(INSTANCE_KEY, it)
        }
    }

class DestroyableCoroutineScope(
    context: CoroutineContext
) : CoroutineScope, InstanceKeeper.Instance {
    override val coroutineContext: CoroutineContext = context

    override fun onDestroy() {
        coroutineContext.cancel()
    }
}