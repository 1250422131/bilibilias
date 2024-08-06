package com.imcys.bilibilias.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import com.imcys.bilibilias.PACKAGE_NAME
import com.imcys.bilibilias.startActivityAndAllowNotifications
import org.junit.Rule
import org.junit.Test

class HomeBaselineProfile {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() =
        baselineProfileRule.collect(PACKAGE_NAME) {
            startActivityAndAllowNotifications()

            // Scroll the feed critical user journey
//            forYouWaitForContent()
//            forYouSelectTopics(true)
//            forYouScrollFeedDownUp()
        }
}
