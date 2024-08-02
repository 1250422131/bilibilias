package com.imcys.bilibilias.core.datastore

import androidx.datastore.core.Serializer
import com.imcys.bilibilias.core.datastore.model.AsCookieStore
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

@OptIn(ExperimentalSerializationApi::class)
internal class AsCookieStoreSerializer @Inject constructor(
    private val protoBuf: ProtoBuf,
) : Serializer<AsCookieStore> {
    override val defaultValue: AsCookieStore = AsCookieStore()

    override suspend fun readFrom(input: InputStream): AsCookieStore =
        protoBuf.decodeFromByteArray(input.readBytes())


    override suspend fun writeTo(t: AsCookieStore, output: OutputStream) {
        output.write(protoBuf.encodeToByteArray(t))
    }
}
