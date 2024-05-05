package com.imcys.bilibilias.util

import android.app.Application
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppCenter @Inject constructor(
    @ApplicationContext private val context: Application
) {
    companion object {
        const val APP_SECRET = "3c7c5174-a6be-4093-a0df-c6fbf7371480"
    }

    operator fun invoke() {
        AppCenter.start(
            context,
            APP_SECRET,
            Analytics::class.java,
            Crashes::class.java
        )
    }
}
