package com.imcys.bilibilias

import android.app.Activity
import android.content.ComponentCallbacks
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import org.koin.android.ext.android.getKoin
import org.koin.android.scope.AndroidScopeComponent
import org.koin.core.component.getScopeId
import org.koin.core.component.getScopeName
import org.koin.core.definition.BeanDefinition
import org.koin.core.definition.Kind
import org.koin.core.instance.SingleInstanceFactory
import org.koin.core.qualifier.TypeQualifier
import org.koin.core.scope.Scope
import org.koin.core.scope.ScopeCallback

inline val <reified T : ComponentActivity> T.activityScope: Scope
    get() = with(this) {
        createActivityScope()
            .addInstance(this)
            .addInstance(this as Activity)
            .addInstance(this as ComponentActivity)
    }

inline fun <reified T> Scope.addInstance(instance: T): Scope {
    val definition = BeanDefinition(
        scopeQualifier = this.scopeQualifier,
        primaryType = T::class,
        kind = Kind.Singleton,
        definition = { instance }
    )
    val factory = SingleInstanceFactory(definition)
    declare(factory)
    return this
}

fun ComponentActivity.createActivityScope(): Scope {
    if (this !is AndroidScopeComponent) {
        error("Activity should implement AndroidScopeComponent")
    }
    return getKoin().getScopeOrNull(getScopeId()) ?: createScopeForCurrentLifecycle(
        this,
        ComponentActivityScopeArchetype
    )
}

internal fun ComponentCallbacks.createScopeForCurrentLifecycle(
    owner: LifecycleOwner,
    scopeArchetype: TypeQualifier
): Scope {
    val scope = getKoin().createScope(getScopeId(), getScopeName(), this, scopeArchetype)
    scope.registerCallback(object : ScopeCallback {
        override fun onScopeClose(scope: Scope) {
            (owner as AndroidScopeComponent).onCloseScope()
        }
    })
    owner.registerScopeForLifecycle(scope)
    return scope
}

internal fun LifecycleOwner.registerScopeForLifecycle(
    scope: Scope
) {
    lifecycle.addObserver(
        object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                scope.close()
                super.onDestroy(owner)
            }
        }
    )
}

val ComponentActivityScopeArchetype = TypeQualifier(ComponentActivity::class)