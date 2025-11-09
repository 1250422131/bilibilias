package com.imcys.bilibilias.common.data


object CommonBuildConfig {
    var enabledAnalytics = false
    var agreedPrivacyPolicy = false
}


inline fun commonAnalyticsSafe(action:  () -> Unit) {
    if (CommonBuildConfig.enabledAnalytics && CommonBuildConfig.agreedPrivacyPolicy) {
        action()
    }
}