package com.imcys.network.configration

import io.ktor.http.Cookie
import io.ktor.http.CookieEncoding
import io.ktor.util.date.GMTDate
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

const val COOKIE_NAME = 0
const val COOKIE_MAXAGE = 2
const val COOKIE_VALUE = 1
const val COOKIE_EXPIRES = 3
const val COOKIE_DOMAIN = 4
const val COOKIE_PATH = 5
const val COOKIE_SECURE = 6
const val COOKIE_HTTPONLY = 7

internal object CookieSerializer : KSerializer<Cookie> {
    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): Cookie =
        decoder.decodeStructure(descriptor) {
            var name = ""
            var value = ""

            var maxAge = 0
            var expires: Long = 0

            var domain: String? = null
            var path: String? = null

            var secure = false
            var httpOnly = false

            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    COOKIE_NAME -> name = decodeStringElement(descriptor, COOKIE_NAME)
                    COOKIE_VALUE -> value = decodeStringElement(descriptor, COOKIE_VALUE)
                    COOKIE_MAXAGE -> maxAge = decodeIntElement(descriptor, COOKIE_MAXAGE)

                    COOKIE_EXPIRES -> expires = decodeLongElement(descriptor, COOKIE_EXPIRES)
                    COOKIE_DOMAIN ->
                        domain =
                            decodeNullableSerializableElement(
                                descriptor,
                                COOKIE_DOMAIN,
                                String.serializer()
                            )

                    COOKIE_PATH ->
                        path =
                            decodeNullableSerializableElement(
                                descriptor,
                                COOKIE_PATH,
                                String.serializer()
                            )

                    COOKIE_SECURE -> secure = decodeBooleanElement(descriptor, COOKIE_SECURE)
                    COOKIE_HTTPONLY -> httpOnly = decodeBooleanElement(descriptor, COOKIE_HTTPONLY)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Cookie(
                name,
                value,
                CookieEncoding.RAW,
                maxAge,
                GMTDate(expires),
                domain,
                path,
                secure,
                httpOnly
            )
        }

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("cookie") {

            element<String>("name")
            element<String>("value")

            element<Int>("maxAge")

            element<Long>("expires")

            element<String>("domain")
            element<String>("path")

            element<Boolean>("secure")
            element<Boolean>("httpOnly")
        }

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: Cookie) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, COOKIE_NAME, value.name)
            encodeStringElement(descriptor, COOKIE_VALUE, value.value)

            encodeNullableSerializableElement(
                descriptor,
                COOKIE_MAXAGE,
                Int.serializer(),
                value.maxAge
            )
            encodeLongElement(descriptor, COOKIE_EXPIRES, value.expires?.timestamp ?: 0)

            encodeNullableSerializableElement(
                descriptor,
                COOKIE_DOMAIN,
                String.serializer(),
                value.domain
            )
            encodeNullableSerializableElement(
                descriptor,
                COOKIE_PATH,
                String.serializer(),
                value.path
            )

            encodeBooleanElement(descriptor, COOKIE_SECURE, value.secure)
            encodeBooleanElement(descriptor, COOKIE_HTTPONLY, value.httpOnly)
        }
    }
}
