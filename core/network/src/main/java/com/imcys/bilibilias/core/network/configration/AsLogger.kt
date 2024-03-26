package com.imcys.bilibilias.core.network.configration

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.aakira.napier.Napier
import io.ktor.client.plugins.logging.Logger
import javax.inject.Inject

class AsLogger @Inject constructor() : Logger {
    override fun log(message: String) {
        Napier.d(tag = "bili-as") { message }
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface LoggerModule {
    @Binds
    fun bindLogger(asLogger: AsLogger): Logger
}