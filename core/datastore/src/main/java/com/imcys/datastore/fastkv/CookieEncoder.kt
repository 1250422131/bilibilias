package com.imcys.datastore.fastkv

import com.imcys.model.login.*
import io.fastkv.interfaces.*
import kotlinx.serialization.*
import kotlinx.serialization.cbor.*

@OptIn(ExperimentalSerializationApi::class)
object CookieEncoder : FastEncoder<MutableList<Cookie>> {
    private val cbor = Cbor
    override fun tag(): String {
        return "CookieEncoder"
    }

    override fun encode(obj: MutableList<Cookie>): ByteArray {
        return cbor.encodeToByteArray(obj)
    }

    override fun decode(bytes: ByteArray, offset: Int, length: Int): MutableList<Cookie> {
        return cbor.decodeFromByteArray(bytes)
    }
}