package com.imcys.bilibilias.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import com.imcys.bilibilias.core.io.SystemPath
import com.imcys.bilibilias.core.json.DataStoreJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

expect interface DataStoreSerializer<T>

expect fun <T> KSerializer<T>.asDataStoreSerializer(
    format: Json = DataStoreJson,
    defaultValue: () -> T,
): DataStoreSerializer<T>

// Datastore 忘了给 expect 加 default constructor
expect fun <T> ReplaceFileCorruptionHandler(produceNewData: (CorruptionException) -> T): ReplaceFileCorruptionHandler<T>

fun <T> DataStoreFactory.create(
    serializer: KSerializer<T>,
    defaultValue: () -> T,
    corruptionHandler: ReplaceFileCorruptionHandler<T>?,
    migrations: List<DataMigration<T>> = listOf(),
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
    produceFile: () -> SystemPath
): DataStore<T> {
    return create(
        serializer = serializer.asDataStoreSerializer(defaultValue = defaultValue),
        corruptionHandler = corruptionHandler,
        migrations = migrations,
        scope = scope,
        produceFile = produceFile,
    )
}

expect fun <T> DataStoreFactory.create(
    serializer: DataStoreSerializer<T>,
    corruptionHandler: ReplaceFileCorruptionHandler<T>?,
    migrations: List<DataMigration<T>> = listOf(),
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
    produceFile: () -> SystemPath
): DataStore<T>