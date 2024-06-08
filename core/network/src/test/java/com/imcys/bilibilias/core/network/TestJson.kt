package com.imcys.bilibilias.core.network

import io.ktor.util.asStream
import io.ktor.utils.io.streams.asInput
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.decodeFromStream
import org.junit.Test

class TestJson {
    @Test
    fun `test json`() {
        val json = """
            {
              "3064": 1,
              "5062": "",
              "03bf": "https%3A%2F%2Fwww.bilibili.com%2F",
              "39c8": "333.788.fp.risk"
            }
        """.trimIndent()
        val j = Json {
            prettyPrint = true
        }
        val payload = j.decodeFromString<JsonObject>(json)
        val s = payload.toMutableMap().apply {
            put("5062", JsonPrimitive(System.currentTimeMillis()))
            put("3064", JsonPrimitive("6666"))
        }
        println(payload)
        println(s)
        println(j.encodeToString(s))
    }
}