package com.imcys.bilibilias.feature.common.molecule

import androidx.compose.runtime.AbstractApplier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.snapshots.ObserverHandle
import androidx.compose.runtime.snapshots.Snapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.cancellation.CancellationException

/**
 * Create a [Flow] which will continually recompose `body` to produce a stream of [T] values
 * when collected.
 */
public fun <T> moleculeFlow(mode: RecompositionMode, body: @Composable () -> T): Flow<T> {
    return when (mode) {
        RecompositionMode.ContextClock -> contextClockFlow(body)
        RecompositionMode.Immediate -> immediateClockFlow(body)
    }
}

private fun <T> contextClockFlow(body: @Composable () -> T) = channelFlow {
    launchMolecule(
        mode = RecompositionMode.ContextClock,
        emitter = {
            trySend(it).getOrThrow()
        },
        body = body,
    )
}

private fun <T> immediateClockFlow(body: @Composable () -> T): Flow<T> = flow {
    coroutineScope {
        val clock = GatedFrameClock(this)
        val outputBuffer = Channel<T>(1)

        launch(clock, start = CoroutineStart.UNDISPATCHED) {
            launchMolecule(
                mode = RecompositionMode.ContextClock,
                emitter = {
                    clock.isRunning = false
                    outputBuffer.trySend(it).getOrThrow()
                },
                body = body,
            )
        }

        while (true) {
            val result = outputBuffer.tryReceive()
            // Per `ReceiveChannel.tryReceive` documentation: isFailure means channel is empty.
            val value = if (result.isFailure) {
                clock.isRunning = true
                outputBuffer.receive()
            } else {
                result.getOrThrow()
            }
            emit(value)
        }
        /*
        TODO: Replace the above block with the following once `ReceiveChannel.isEmpty` is stable:

        for (item in outputBuffer) {
          emit(item)
          if (outputBuffer.isEmpty) {
            clock.isRunning = true
          }
        }
         */
    }
}

/**
 * Launch a coroutine into this [CoroutineScope] which will continually recompose `body`
 * to produce a [StateFlow] stream of [T] values.
 *
 * The coroutine context is inherited from the [CoroutineScope].
 * Additional context elements can be specified with [context] argument.
 */
public fun <T> CoroutineScope.launchMolecule(
    mode: RecompositionMode,
    context: CoroutineContext = EmptyCoroutineContext,
    body: @Composable () -> T,
): StateFlow<T> {
    var flow: MutableStateFlow<T>? = null

    launchMolecule(
        context = context,
        mode = mode,
        emitter = { value ->
            val outputFlow = flow
            if (outputFlow != null) {
                outputFlow.value = value
            } else {
                flow = MutableStateFlow(value)
            }
        },
        body = body,
    )

    return flow!!
}

/**
 * Launch a coroutine into this [CoroutineScope] which will continually recompose `body`
 * in the optional [context] to invoke [emitter] with each returned [T] value.
 *
 * [launchMolecule]'s [emitter] is always free-running and will not respect backpressure.
 * Use [moleculeFlow] to create a backpressure-capable flow.
 *
 * The coroutine context is inherited from the [CoroutineScope].
 * Additional context elements can be specified with [context] argument.
 */
public fun <T> CoroutineScope.launchMolecule(
    mode: RecompositionMode,
    emitter: (value: T) -> Unit,
    context: CoroutineContext = EmptyCoroutineContext,
    body: @Composable () -> T,
) {
    val clockContext = when (mode) {
        RecompositionMode.ContextClock -> EmptyCoroutineContext
        RecompositionMode.Immediate -> GatedFrameClock(this)
    }
    val finalContext = coroutineContext + context + clockContext

    val recomposer = Recomposer(finalContext)
    val composition = Composition(UnitApplier, recomposer)
    var snapshotHandle: ObserverHandle? = null
    launch(finalContext, start = CoroutineStart.UNDISPATCHED) {
        try {
            recomposer.runRecomposeAndApplyChanges()
        } catch (e: CancellationException) {
            composition.dispose()
            snapshotHandle?.dispose()
        }
    }

    var applyScheduled = false
    snapshotHandle = Snapshot.registerGlobalWriteObserver {
        if (!applyScheduled) {
            applyScheduled = true
            launch(finalContext) {
                applyScheduled = false
                Snapshot.sendApplyNotifications()
            }
        }
    }

    composition.setContent {
        emitter(body())
    }
}

private object UnitApplier : AbstractApplier<Unit>(Unit) {
    override fun insertBottomUp(index: Int, instance: Unit) = Unit
    override fun insertTopDown(index: Int, instance: Unit) = Unit
    override fun move(from: Int, to: Int, count: Int) = Unit
    override fun remove(index: Int, count: Int) = Unit
    override fun onClear() = Unit
}
