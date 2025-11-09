package com.imcys.bilibilias.common.utils

import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ParametersBuilder
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent

object FirebaseExt {
    fun logLogin(method: String) {
        firebaseLog(FirebaseAnalytics.Event.LOGIN) {
            param(FirebaseAnalytics.Param.METHOD, method)
        }
    }

    fun logVideoParse(
        bvId: String?,
    ) {
        firebaseLog("parse_video") {
            bvId?.let { param("bvid", it) }
        }
    }

    fun logBangumiParse(
        epId: Long? = null,
        ssId: Long? = null,
    ) {
        firebaseLog("parse_bangumi") {
            epId?.let { param("epId", it) }
            ssId?.let { param("ssId", it) }
        }
    }
}

fun firebaseLog(name: String, block: ParametersBuilder.() -> Unit) {
    analyticsSafe {
        Firebase.analytics.logEvent(name) {
            block()
        }
    }
}