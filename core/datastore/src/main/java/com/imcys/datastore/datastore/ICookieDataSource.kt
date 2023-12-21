package com.imcys.datastore.datastore

import com.bilias.core.datastore.AsCookie
import kotlinx.collections.immutable.ImmutableList
import java.io.Closeable

interface ICookieDataSource : Closeable {
    fun get(name: String): AsCookie?

    fun set(name: String, value: AsCookie)
    fun getAll(): ImmutableList<AsCookie>
}
