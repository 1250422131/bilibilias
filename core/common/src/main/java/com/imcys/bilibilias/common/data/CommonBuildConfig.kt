package com.imcys.bilibilias.common.data


object CommonBuildConfig {
    var enabledAnalytics = false
}


inline fun commonAnalyticsSafe(action:  () -> Unit) {
    if (CommonBuildConfig.enabledAnalytics) {
        action()
    }
}