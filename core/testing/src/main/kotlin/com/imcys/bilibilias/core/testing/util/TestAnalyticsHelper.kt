package com.imcys.bilibilias.core.testing.util

import com.imcys.bilibilias.core.analytics.AnalyticsEvent
import com.imcys.bilibilias.core.analytics.AnalyticsHelper

class TestAnalyticsHelper : AnalyticsHelper {

    private val events = mutableListOf<AnalyticsEvent>()
    override fun logEvent(event: AnalyticsEvent) {
        events.add(event)
    }

    fun hasLogged(event: AnalyticsEvent) = event in events
}
