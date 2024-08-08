package com.imcys.bilibilias.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.imcys.bilibilias.core.common.network.AsDispatchers
import com.imcys.bilibilias.core.common.network.Dispatcher
import com.imcys.bilibilias.core.datastore.di.protobuf
import com.imcys.bilibilias.core.datastore.model.AsCookieStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

@OptIn(ExperimentalSerializationApi::class)
internal class AsCookieStoreSerializer @Inject constructor(
    @Dispatcher(AsDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : Serializer<AsCookieStore> {
    override val defaultValue: AsCookieStore = AsCookieStore()

    override suspend fun readFrom(input: InputStream): AsCookieStore = try {
        protobuf.decodeFromByteArray(input.readBytes())
    } catch (e: SerializationException) {
        throw CorruptionException("Cannot read stored data", e)
    } catch (e: IllegalArgumentException) {
        throw CorruptionException("Cannot read stored data", e)
    }

    override suspend fun writeTo(t: AsCookieStore, output: OutputStream) {
        withContext(ioDispatcher) {
            output.write(protobuf.encodeToByteArray(t))
        }
    }
}
