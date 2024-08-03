package com.imcys.network.configration

import android.util.Log
import timber.log.Timber

internal object NetworkTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return
        }

        if (t != null) {
            if (priority == Log.ERROR) {

            } else if (priority == Log.WARN) {

            }
        }
    }
}