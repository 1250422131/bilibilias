package com.imcys.bilibilias.core.analytics

/**
 * Interface for logging analytics events. See`StubAnalyticsHelper` for implementations.
 */
interface AnalyticsHelper {
    fun logEvent(event: AnalyticsEvent)
}
