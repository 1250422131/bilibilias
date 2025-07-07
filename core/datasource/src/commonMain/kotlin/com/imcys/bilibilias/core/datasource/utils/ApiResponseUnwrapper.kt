package com.imcys.bilibilias.core.datasource.utils

import com.imcys.bilibilias.core.datasource.model.Box
import com.imcys.bilibilias.core.json.HttpClientJson
import io.ktor.client.plugins.api.ClientPlugin
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.serializer

@OptIn(ExperimentalSerializationApi::class)
val ApiResponseUnwrapper: ClientPlugin<Unit> =
    createClientPlugin("ApiResponseUnwrapperPlugin") {
        transformResponseBody { response, content, requestedType ->
            val kotlinType = requestedType.kotlinType ?: return@transformResponseBody null
            val box = HttpClientJson.decodeFromStream(
                Box.serializer(serializer(kotlinType)),
                content.toInputStream()
            )
            box.data
        }
    }