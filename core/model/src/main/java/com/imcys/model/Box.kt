package com.imcys.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class Box<T>(val code: Int, val message: String, val data: T)

class BoxSerializer<T>(private val dataSerializer: KSerializer<T>) : KSerializer<Box<T>> {
    override val descriptor: SerialDescriptor = dataSerializer.descriptor
    override fun serialize(encoder: Encoder, value: Box<T>) {
        encoder.encodeInt(value.code)
        encoder.encodeString(value.message)
        dataSerializer.serialize(encoder, value.data)
    }

    override fun deserialize(decoder: Decoder) = Box(
        decoder.decodeInt(),
        decoder.decodeString(),
        dataSerializer.deserialize(decoder)
    )
}