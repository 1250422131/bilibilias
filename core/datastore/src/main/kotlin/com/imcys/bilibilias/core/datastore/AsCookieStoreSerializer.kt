package com.imcys.bilibilias.core.datastore

import androidx.datastore.core.Serializer
import com.imcys.bilibilias.core.common.network.AsDispatchers
import com.imcys.bilibilias.core.common.network.Dispatcher
import com.imcys.bilibilias.core.datastore.model.AsCookieStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
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
    @Dispatcher(AsDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : Serializer<AsCookieStore> {
    override val defaultValue: AsCookieStore = AsCookieStore()

    override suspend fun readFrom(input: InputStream): AsCookieStore =
        protoBuf.decodeFromByteArray(input.readBytes())

    override suspend fun writeTo(t: AsCookieStore, output: OutputStream) {
        withContext(ioDispatcher) {
            output.write(protoBuf.encodeToByteArray(t))
        }
    }
}
