package com.imcys.datastore.fastkv

import com.imcys.model.cookie.AsCookie
import io.fastkv.interfaces.FastEncoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import javax.inject.Inject

@OptIn(ExperimentalSerializationApi::class)
class CookieListEncoder @Inject constructor(private val cbor: Cbor) : FastEncoder<MutableList<AsCookie>> {
    override fun tag(): String = "AsCookie"
    override fun decode(bytes: ByteArray, offset: Int, length: Int): MutableList<AsCookie> =
        cbor.decodeFromByteArray(bytes)

    override fun encode(obj: MutableList<AsCookie>): ByteArray =
        cbor.encodeToByteArray(obj)
}