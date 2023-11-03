package com.imcys.datastore.fastkv

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.fastkv.FastKV
import io.fastkv.FastKVConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import javax.inject.Inject

open class GlobalStorage @Inject constructor(name: String) : KVData() {

    @Inject
    @ApplicationContext
    lateinit var context: Context

    init {
        FastKVConfig.setExecutor(Dispatchers.IO.asExecutor())
    }

    override val kv: FastKV by lazy {
        buildKV(context, name)
    }


}