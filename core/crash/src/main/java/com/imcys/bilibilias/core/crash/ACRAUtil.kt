package com.imcys.bilibilias.core.crash

import android.app.Application
import org.acra.config.dialog
import org.acra.data.StringFormat
import org.acra.ktx.initAcra

object ACRAUtil {
    fun init(app: Application) {
        app.init()
    }

    fun Application.init() {
        initAcra {
            reportFormat = StringFormat.JSON

            dialog {
                enabled = true
                text = "dialog"
            }
        }
    }
}
