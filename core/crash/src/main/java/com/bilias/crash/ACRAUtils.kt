package com.bilias.crash

import android.app.Application
import org.acra.ACRA
import org.acra.config.dialog
import org.acra.config.httpSender
import org.acra.config.limiter
import org.acra.config.mailSender
import org.acra.config.notification
import org.acra.config.scheduler
import org.acra.config.toast
import org.acra.data.StringFormat
import org.acra.ktx.initAcra
import org.acra.security.TLS
import org.acra.sender.HttpSender

object ACRAUtils {
    fun trackBreadcrumb(event: String) {
        ACRA.errorReporter.putCustomData("Event at ${System.currentTimeMillis()}", event)
    }

    context(Application)
    fun init() {
        initAcra {
            buildConfigClass = com.bilias.crash.BuildConfig::class.java
            reportFormat = StringFormat.JSON
            toast {
                text = "应用崩溃了"
            }
            httpSender {
                enabled = false
                //required. Https recommended
                uri = "https://your.server.com/report"
                //optional. Enables http basic auth
                basicAuthLogin = "acra"
                //required if above set
                basicAuthPassword = "password"
                // defaults to POST
                httpMethod = HttpSender.Method.POST
                //defaults to 5000ms
                connectionTimeout = 5000
                //defaults to 20000ms
                socketTimeout = 20000
                // defaults to false
                dropReportsOnTimeout = false
                //使用以下选项可以配置自签名证书
//                keyStoreFactoryClass = MyKeyStoreFactory::class.java
//                certificatePath = "asset://mycert.cer"
//                resCertificate = R.raw.mycert
//                certificateType = "X.509"
                //默认为false。建议您的后端支持它
                compress = true
                tlsProtocols = listOf(TLS.V1_3, TLS.V1_2, TLS.V1_1, TLS.V1)
            }
            mailSender {
                enabled = false
                //required
                mailTo = "acra@yourserver.com"
                //defaults to true
                reportAsFile = true
                //defaults to ACRA-report.stacktrace
                reportFileName = "Crash.txt"
                //defaults to "<applicationId> Crash Report"
//                subject = getString(R.string.mail_subject)
                //defaults to empty
//                body = getString(R.string.mail_body)
            }
            scheduler {

            }
            limiter {

            }
            dialog {
                enabled = false
                text = "dialog"
            }
            notification {
                enabled = false
                title = "title"
                text = "text"
                channelName = "channelName"
            }
        }
    }
}