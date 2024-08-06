package com.imcys.bilibilias.core.network

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test

class TestJson {
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    @Test
    fun `test json`() {
        val text = """
            {
              "3064": 1,
              "5062": "",
              "03bf": "https%3A%2F%2Fwww.bilibili.com%2F",
              "39c8": "333.788.fp.risk"
            }
        """.trimIndent()
        val payload = json.decodeFromString<JsonObject>(text)
        val s = payload.toMutableMap().apply {
            put("5062", JsonPrimitive(System.currentTimeMillis()))
            put("3064", JsonPrimitive("6666"))
        }
        println(payload)
        println(s)
        println(json.encodeToString(s))
    }
}