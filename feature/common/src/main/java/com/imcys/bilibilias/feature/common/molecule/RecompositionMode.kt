package com.imcys.bilibilias.feature.common.molecule

/** The different recomposition modes of Molecule. */
public enum class RecompositionMode {
    /**
     * When a recomposition is needed, use a [MonotonicFrameClock] pulled from the calling [CoroutineContext]
     * to determine when to run. If no clock is found in the context, an exception is thrown.
     *
     * Use this option to drive Molecule with a built-in frame clock or a custom one.
     */
    ContextClock,

    /**
     * Run recomposition eagerly whenever one is needed.
     * Molecule will emit a new item every time the snapshot state is invalidated.
     */
    Immediate,
}
