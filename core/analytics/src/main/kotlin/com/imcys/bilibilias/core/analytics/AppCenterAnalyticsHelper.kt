package com.imcys.bilibilias.core.analytics

import com.microsoft.appcenter.analytics.Analytics
import javax.inject.Inject

internal class AppCenterAnalyticsHelper @Inject constructor() : AnalyticsHelper {

    override fun logEvent(event: AnalyticsEvent) {
        Analytics.trackEvent(event.type, event.extras.associate { it.key to it.value })
    }
}
