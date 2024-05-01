package com.imcys.bilibilias.core.crash

import android.app.Application
import org.acra.config.dialog
import org.acra.data.StringFormat
import org.acra.ktx.initAcra

object ACRAUtil {
    fun init(app: Application) {
        app.initAcra()
    }

    private fun Application.initAcra() {
        initAcra {
            reportFormat = StringFormat.JSON

            dialog {
                enabled = false
                text = "dialog"
            }
        }
    }
}
