package com.imcys.bilibilias.home.ui.model.serializer

import com.imcys.bilibilias.home.ui.model.DashVideoPlayBean.DataBean.DashBean.AudioBean as AudioBean
import kotlinx.serialization.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

// 自定义序列化器，用于处理特定字段的null值强制转换
class AudioBeanNullSerializer : KSerializer<List<AudioBean>> {
    private val audioBeanSerializer = AudioBean.serializer()
    private val listSerializer = ListSerializer(audioBeanSerializer)

    override val descriptor: SerialDescriptor = listSerializer.descriptor

    override fun deserialize(decoder: Decoder): List<AudioBean> {
        val input = decoder as? JsonDecoder ?: return listSerializer.deserialize(decoder)
        val element = input.decodeJsonElement()

        // If it's null, return empty list
        if (element is JsonNull) {
            return emptyList()
        }

        // If it's not an array (unexpected format), handle it gracefully
        if (element !is JsonArray) {
            return emptyList()
        }

        // Otherwise, decode the array properly
        return input.json.decodeFromJsonElement(listSerializer, element)
    }

    override fun serialize(encoder: Encoder, value: List<AudioBean>) {
        listSerializer.serialize(encoder, value)
    }
}