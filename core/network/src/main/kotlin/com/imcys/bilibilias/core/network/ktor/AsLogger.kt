package com.imcys.bilibilias.core.network.ktor

import com.imcys.bilibilias.core.network.ktor.plugin.logging.JsonAwareLogger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.aakira.napier.Napier
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class AsLoggerImpl @Inject constructor(private val json: Json) : JsonAwareLogger {

    override fun prettifyJson(message: String): String {
        return json.encodeToString(json.parseToJsonElement(message))
    }

    override fun log(message: String) {
        Napier.i(tag = "Ktor Client") { message }
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal interface Logger {
    @Binds
    fun bindLogger(asLogger: AsLoggerImpl): JsonAwareLogger
}
