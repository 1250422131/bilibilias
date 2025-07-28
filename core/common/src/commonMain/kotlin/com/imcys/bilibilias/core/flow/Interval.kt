package com.imcys.bilibilias.core.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration

/**
 * Returns a [Flow] that emits a 0L after the [initialDelay] and ever-increasing numbers
 * after each [period] of time thereafter.
 *
 * @param initialDelay must be greater than or equal to [Duration.ZERO]
 * @param period must be greater than or equal to [Duration.ZERO]
 */
public fun interval(
    period: Duration,
    initialDelay: Duration = Duration.ZERO,
): Flow<Long> {
    require(initialDelay >= Duration.ZERO) { "Expected non-negative delay, but has $initialDelay ms" }
    require(period >= Duration.ZERO) { "Expected non-negative period, but has $period ms" }

    return flow {
        delay(initialDelay)

        var count = 0L
        while (true) {
            emit(count++)
            delay(period)
        }
    }
}

/**
 * Returns a [Flow] that emits a 0L after the [initialDelayMillis] and ever-increasing numbers
 * after each [periodMillis] of time thereafter.
 *
 * @param initialDelayMillis must be non-negative
 * @param periodMillis must be non-negative
 */
public fun interval(
    periodMillis: Long,
    initialDelayMillis: Long = 0,
): Flow<Long> {
    require(initialDelayMillis >= 0) { "Expected non-negative delay, but has $initialDelayMillis ms" }
    require(periodMillis >= 0) { "Expected non-negative periodMillis, but has $periodMillis ms" }

    return flow {
        delay(initialDelayMillis)

        var count = 0L
        while (true) {
            emit(count++)
            delay(periodMillis)
        }
    }
}