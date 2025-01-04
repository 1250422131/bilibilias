package com.imcys.bilibilias.common.di

import io.github.aakira.napier.Napier
import io.ktor.client.plugins.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AsLogger @Inject constructor() : Logger {
    override fun log(message: String) {
        Napier.d(message, tag = "AsLogger")
    }
}
