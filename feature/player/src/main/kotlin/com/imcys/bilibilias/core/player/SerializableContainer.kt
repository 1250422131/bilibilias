package com.imcys.bilibilias.core.player

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

@Serializable(with = SerializableContainer.Serializer::class)
class SerializableContainer private constructor(
    private var data: ByteArray?,
) {
    constructor() : this(data = null)

    private var holder: Holder<*>? = null

    /**
     * Deserializes and returns a previously stored [Serializable][kotlinx.serialization.Serializable] object.
     *
     * @param strategy a [DeserializationStrategy] for deserializing the object.
     */
    fun <T : Any> consume(strategy: DeserializationStrategy<T>): T? {
        val consumedValue: Any? = holder?.value ?: data?.deserialize(strategy)
        holder = null
        data = null

        @Suppress("UNCHECKED_CAST")
        return consumedValue as T?
    }

    /**
     * Stores a [Serializable][kotlinx.serialization.Serializable] object, replacing any previously stored object.
     *
     * @param value an object to be stored and serialized later when needed.
     * @param strategy a [SerializationStrategy] for serializing the value.
     */
    fun <T : Any> set(value: T?, strategy: SerializationStrategy<T>) {
        holder = Holder(value = value, strategy = strategy)
        data = null
    }

    /**
     * Clears any previously stored object.
     */
    fun clear() {
        holder = null
        data = null
    }

    private class Holder<T : Any>(
        val value: T?,
        val strategy: SerializationStrategy<T>,
    )

    internal object Serializer : KSerializer<SerializableContainer> {
        private const val NULL_MARKER = "."
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("SerializableContainer", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: SerializableContainer) {
            val bytes = value.holder?.serialize() ?: value.data
            encoder.encodeString(bytes?.toBase64() ?: NULL_MARKER)
        }

        private fun <T : Any> Holder<T>.serialize(): ByteArray? =
            value?.serialize(strategy)

        override fun deserialize(decoder: Decoder): SerializableContainer =
            SerializableContainer(
                data = decoder.decodeString().takeUnless { it == NULL_MARKER }
                    ?.base64ToByteArray()
            )
    }
}

/**
 * Creates a new [SerializableContainer] and sets the provided [value] with the provided [strategy].
 */
fun <T : Any> SerializableContainer(
    value: T?,
    strategy: SerializationStrategy<T>
): SerializableContainer =
    SerializableContainer().apply {
        set(value = value, strategy = strategy)
    }

/**
 * A convenience method for [SerializableContainer.consume]. Throws [IllegalStateException]
 * if the [SerializableContainer] is empty.
 */
fun <T : Any> SerializableContainer.consumeRequired(strategy: DeserializationStrategy<T>): T =
    checkNotNull(consume(strategy))

internal fun ByteArray.toBase64(): String =
    encode(this)

internal fun encode(array: ByteArray): String = buildString(capacity = (array.size / 3) * 4 + 1) {
    var index = 0

    while (index < array.size) {
        if (index + 3 > array.size) break

        val buffer = array[index].toInt() and 0xff shl 16 or
                (array[index + 1].toInt() and 0xff shl 8) or
                (array[index + 2].toInt() and 0xff shl 0)

        append(dictionary[buffer shr 18])
        append(dictionary[buffer shr 12 and 0x3f])
        append(dictionary[buffer shr 6 and 0x3f])
        append(dictionary[buffer and 0x3f])

        index += 3
    }

    if (index < array.size) {
        var buffer = 0
        while (index < array.size) {
            buffer = buffer shl 8 or (array[index].toInt() and 0xff)
            index++
        }
        val padding = 3 - (index % 3)
        buffer = buffer shl (padding * 8)

        append(dictionary[buffer shr 18])
        append(dictionary[buffer shr 12 and 0x3f])

        val a = dictionary[buffer shr 6 and 0x3f]
        val b = dictionary[buffer and 0x3f]

        when (padding) {
            0 -> {
                append(a)
                append(b)
            }

            1 -> {
                append(a)
                append('=')
            }

            2 -> {
                append("==")
            }
        }
    }
}

internal val dictionary: CharArray =
    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray()

internal val backDictionary: IntArray = IntArray(0x80) { code ->
    dictionary.indexOf(code.toChar())
}

internal fun String.base64ToByteArray(): ByteArray =
    decode(this)

@Suppress("CognitiveComplexMethod", "LoopWithTooManyJumpStatements") // Keep the original
internal fun decode(encoded: String): ByteArray {
    if (encoded.isBlank()) return ByteArray(0)
    val result = ByteArray(encoded.length)
    var resultSize = 0

    val backDictionary = backDictionary
    var buffer = 0
    var buffered = 0
    var index = 0

    while (index < encoded.length) {
        val ch = encoded[index++]
        if (ch <= ' ') continue
        if (ch == '=') {
            index--
            break
        }
        val value = backDictionary.getOrElse(ch.code) { -1 }
        if (value == -1) error("Unexpected character $ch (${ch.code})) in $encoded")

        buffer = buffer shl 6 or value
        buffered++

        if (buffered == 4) {
            result[resultSize] = (buffer shr 16).toByte()
            result[resultSize + 1] = (buffer shr 8 and 0xff).toByte()
            result[resultSize + 2] = (buffer and 0xff).toByte()
            resultSize += 3
            buffered = 0
            buffer = 0
        }
    }

    var padding = 0
    while (index < encoded.length) {
        val ch = encoded[index++]
        if (ch <= ' ') continue
        check(ch == '=')
        padding++
        buffer = buffer shl 6
        buffered++
    }

    if (buffered == 4) {
        result[resultSize] = (buffer shr 16).toByte()
        result[resultSize + 1] = (buffer shr 8 and 0xff).toByte()
        result[resultSize + 2] = (buffer and 0xff).toByte()
        resultSize += 3

        resultSize -= padding
        buffered = 0
    }

    check(buffered == 0) {
        "buffered: $buffered"
    }

    return when {
        resultSize < result.size -> result.copyOf(resultSize)
        else -> result
    }
}

internal fun <T> T.serialize(strategy: SerializationStrategy<T>): ByteArray =
    ByteArrayOutputStream().use { output ->
        ZipOutputStream(output).use { zip ->
            zip.setLevel(7)
            zip.putNextEntry(ZipEntry("Entry"))
            zip.buffered().use { bufferedOutput ->
                @kotlin.OptIn(ExperimentalSerializationApi::class)
                Json.encodeToStream(serializer = strategy, value = this, stream = bufferedOutput)
            }
        }

        output.toByteArray()
    }

internal fun <T> ByteArray.deserialize(strategy: DeserializationStrategy<T>): T =
    ZipInputStream(ByteArrayInputStream(this)).use { zip ->
        zip.nextEntry

        zip.buffered().use { bufferedInput ->
            @kotlin.OptIn(ExperimentalSerializationApi::class)
            Json.decodeFromStream(deserializer = strategy, stream = bufferedInput)
        }
    }
