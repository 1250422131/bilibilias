package com.imcys.bilibilias.network.model.serializer

import com.imcys.bilibilias.network.model.BiliApiResponse
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive

/**
 * BiliApiResponse 的自定义序列化器，处理非标准响应格式
 */
class BiliApiResponseSerializer<T>(
    private val dataSerializer: KSerializer<T>
) : KSerializer<BiliApiResponse<T>> {

    override val descriptor: SerialDescriptor =
        BiliApiResponseSurrogate.serializer(dataSerializer).descriptor

    override fun deserialize(decoder: Decoder): BiliApiResponse<T> {
        val input = decoder as? JsonDecoder
            ?: throw SerializationException("BiliApiResponseSerializer can be used only with JSON")

        val element = input.decodeJsonElement()
        val normalized = normalizeToWrapper(element)

        val surrogate = input.json.decodeFromJsonElement(
            BiliApiResponseSurrogate.serializer(dataSerializer),
            normalized
        )

        return BiliApiResponse(
            code = surrogate.code ?: 0,
            data = surrogate.data,
            result = surrogate.result,
            message = surrogate.message,
            ttl = surrogate.ttl ?: 0
        )
    }

    override fun serialize(encoder: Encoder, value: BiliApiResponse<T>) {
        val output = encoder as? JsonEncoder
            ?: throw SerializationException("BiliApiResponseSerializer can be used only with JSON")

        val surrogate = BiliApiResponseSurrogate(
            code = value.code,
            data = value.data,
            result = value.result,
            message = value.message,
            ttl = value.ttl
        )

        output.encodeSerializableValue(
            BiliApiResponseSurrogate.serializer(dataSerializer),
            surrogate
        )
    }

    private fun normalizeToWrapper(element: JsonElement): JsonObject {

        val obj = element as? JsonObject ?: return buildJsonObject {
            put("code", JsonPrimitive(0))
            put("data", element)
            put("message", JsonNull)
            put("ttl", JsonPrimitive(0))
        }

        val looksLikeWrapperByShape = ("data" in obj) ||
                (("result" in obj) && (obj["result"] is JsonObject))
        if (looksLikeWrapperByShape) return obj

        val codeFromTop = obj["code"]?.jsonPrimitive?.intOrNull
        val msgFromTop = obj["message"]?.jsonPrimitive?.contentOrNull
        val ttlFromTop = obj["ttl"]?.jsonPrimitive?.intOrNull

        val dataElement: JsonElement = obj

        return buildJsonObject {
            put("code", JsonPrimitive(codeFromTop ?: 0))
            put("data", dataElement)
            put("message", msgFromTop?.let(::JsonPrimitive) ?: JsonNull)
            put("ttl", JsonPrimitive(ttlFromTop ?: 0))
        }
    }

    @Serializable
    private data class BiliApiResponseSurrogate<T>(
        val code: Int? = null,
        val data: T? = null,
        val result: T? = null,
        val message: String? = null,
        val ttl: Int? = null
    )
}