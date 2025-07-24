package com.imcys.bilibilias.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.coroutines.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Collects values from this [StateFlow] and represents its latest value via [State] in a
 * lifecycle-aware manner.
 *
 * The [StateFlow.value] is used as an initial value. Every time there would be new value posted
 * into the [StateFlow] the returned [State] will be updated causing recomposition of every
 * [State.value] usage whenever the [lifecycleOwner]'s lifecycle is at least [minActiveState].
 *
 * This [StateFlow] is collected every time the [lifecycleOwner]'s lifecycle reaches the
 * [minActiveState] Lifecycle state. The collection stops when the [lifecycleOwner]'s lifecycle
 * falls below [minActiveState].
 *
 * @sample androidx.lifecycle.compose.samples.StateFlowCollectAsStateWithLifecycle
 *
 * Warning: [Lifecycle.State.INITIALIZED] is not allowed in this API. Passing it as a parameter will
 * throw an [IllegalArgumentException].
 *
 * @param lifecycleOwner [LifecycleOwner] whose `lifecycle` is used to restart collecting `this`
 *   flow.
 * @param minActiveState [Lifecycle.State] in which the upstream flow gets collected. The collection
 *   will stop if the lifecycle falls below that state, and will restart if it's in that state
 *   again.
 * @param context [CoroutineContext] to use for collecting.
 */
@Composable
@Suppress("StateFlowValueCalledInComposition")
public // Initial value for an ongoing collect.
fun <T> StateFlow<T>.collectAsStateWithLifecycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext,
): State<T> =
    collectAsStateWithLifecycle(
        initialValue = this.value,
        lifecycle = lifecycleOwner.lifecycle,
        minActiveState = minActiveState,
        context = context,
    )

/**
 * Collects values from this [StateFlow] and represents its latest value via [State] in a
 * lifecycle-aware manner.
 *
 * The [StateFlow.value] is used as an initial value. Every time there would be new value posted
 * into the [StateFlow] the returned [State] will be updated causing recomposition of every
 * [State.value] usage whenever the [lifecycle] is at least [minActiveState].
 *
 * This [StateFlow] is collected every time [lifecycle] reaches the [minActiveState] Lifecycle
 * state. The collection stops when [lifecycle] falls below [minActiveState].
 *
 * @sample androidx.lifecycle.compose.samples.StateFlowCollectAsStateWithLifecycle
 *
 * Warning: [Lifecycle.State.INITIALIZED] is not allowed in this API. Passing it as a parameter will
 * throw an [IllegalArgumentException].
 *
 * @param lifecycle [Lifecycle] used to restart collecting `this` flow.
 * @param minActiveState [Lifecycle.State] in which the upstream flow gets collected. The collection
 *   will stop if the lifecycle falls below that state, and will restart if it's in that state
 *   again.
 * @param context [CoroutineContext] to use for collecting.
 */
@Composable
@Suppress("StateFlowValueCalledInComposition")
public // Initial value for an ongoing collect.
fun <T> StateFlow<T>.collectAsStateWithLifecycle(
    lifecycle: Lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext,
): State<T> =
    collectAsStateWithLifecycle(
        initialValue = this.value,
        lifecycle = lifecycle,
        minActiveState = minActiveState,
        context = context,
    )

/**
 * Collects values from this [Flow] and represents its latest value via [State] in a lifecycle-aware
 * manner.
 *
 * Every time there would be new value posted into the [Flow] the returned [State] will be updated
 * causing recomposition of every [State.value] usage whenever the [lifecycleOwner]'s lifecycle is
 * at least [minActiveState].
 *
 * This [Flow] is collected every time the [lifecycleOwner]'s lifecycle reaches the [minActiveState]
 * Lifecycle state. The collection stops when the [lifecycleOwner]'s lifecycle falls below
 * [minActiveState].
 *
 * @sample androidx.lifecycle.compose.samples.FlowCollectAsStateWithLifecycle
 *
 * Warning: [Lifecycle.State.INITIALIZED] is not allowed in this API. Passing it as a parameter will
 * throw an [IllegalArgumentException].
 *
 * @param initialValue The initial value given to the returned [State.value].
 * @param lifecycleOwner [LifecycleOwner] whose `lifecycle` is used to restart collecting `this`
 *   flow.
 * @param minActiveState [Lifecycle.State] in which the upstream flow gets collected. The collection
 *   will stop if the lifecycle falls below that state, and will restart if it's in that state
 *   again.
 * @param context [CoroutineContext] to use for collecting.
 */
@Composable
public fun <T> Flow<T>.collectAsStateWithLifecycle(
    initialValue: T,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext,
): State<T> =
    collectAsStateWithLifecycle(
        initialValue = initialValue,
        lifecycle = lifecycleOwner.lifecycle,
        minActiveState = minActiveState,
        context = context,
    )

/**
 * Collects values from this [Flow] and represents its latest value via [State] in a lifecycle-aware
 * manner.
 *
 * Every time there would be new value posted into the [Flow] the returned [State] will be updated
 * causing recomposition of every [State.value] usage whenever the [lifecycle] is at least
 * [minActiveState].
 *
 * This [Flow] is collected every time [lifecycle] reaches the [minActiveState] Lifecycle state. The
 * collection stops when [lifecycle] falls below [minActiveState].
 *
 * @sample androidx.lifecycle.compose.samples.FlowCollectAsStateWithLifecycle
 *
 * Warning: [Lifecycle.State.INITIALIZED] is not allowed in this API. Passing it as a parameter will
 * throw an [IllegalArgumentException].
 *
 * @param initialValue The initial value given to the returned [State.value].
 * @param lifecycle [Lifecycle] used to restart collecting `this` flow.
 * @param minActiveState [Lifecycle.State] in which the upstream flow gets collected. The collection
 *   will stop if the lifecycle falls below that state, and will restart if it's in that state
 *   again.
 * @param context [CoroutineContext] to use for collecting.
 */
@Composable
public fun <T> Flow<T>.collectAsStateWithLifecycle(
    initialValue: T,
    lifecycle: Lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext,
): State<T> {
    return produceState(initialValue, this, lifecycle, minActiveState, context) {
        lifecycle.repeatOnLifecycle(minActiveState) {
            if (context == EmptyCoroutineContext) {
                this@collectAsStateWithLifecycle.collect { this@produceState.value = it }
            } else
                withContext(context) {
                    this@collectAsStateWithLifecycle.collect { this@produceState.value = it }
                }
        }
    }
}