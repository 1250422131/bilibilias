package com.imcys.bilibilias.core.analytics

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "StubAnalyticsHelper"

/**
 * An implementation of AnalyticsHelper just writes the events to logcat. Used in builds where no
 * analytics events should be sent to a backend.
 */
@Singleton
class StubAnalyticsHelper @Inject constructor() : AnalyticsHelper {
    override fun logEvent(event: AnalyticsEvent) {
        Log.d(TAG, "Received analytics event: $event")
    }
}
