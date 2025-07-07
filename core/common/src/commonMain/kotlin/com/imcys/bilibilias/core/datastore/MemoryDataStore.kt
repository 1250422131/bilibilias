package com.imcys.bilibilias.core.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

// See also `mutablePreferencesOf` from commonTest.
class MemoryDataStore<T>(initial: T) : DataStore<T> {
    override val data: MutableStateFlow<T> = MutableStateFlow(initial)
    private val lock = Mutex()
    override suspend fun updateData(transform: suspend (t: T) -> T): T {
        lock.withLock {
            val newData = transform(data.value)
            data.value = newData
            return newData
        }
    }
}