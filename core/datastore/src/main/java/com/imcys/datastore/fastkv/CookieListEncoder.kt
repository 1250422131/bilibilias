package com.imcys.datastore.fastkv

import io.fastkv.interfaces.FastEncoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import javax.inject.Inject

@OptIn(ExperimentalSerializationApi::class)
class CookieListEncoder @Inject constructor(private val cbor: Cbor) : FastEncoder<List<ByteArray>> {
    override fun tag(): String = "AsCookie"
    override fun decode(bytes: ByteArray, offset: Int, length: Int): List<ByteArray> =
        cbor.decodeFromByteArray(bytes)

    override fun encode(obj: List<ByteArray>): ByteArray =
        cbor.encodeToByteArray(obj)
}