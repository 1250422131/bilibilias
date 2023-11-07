package com.imcys.datastore.fastkv

import android.content.Context
import com.imcys.common.di.AsDispatchers
import com.imcys.common.di.Dispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import io.fastkv.FastKV
import io.fastkv.FastKVConfig
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asExecutor
import javax.inject.Inject

open class GlobalStorage @Inject constructor(
    name: String,
    @ApplicationContext context: Context,
    @Dispatcher(AsDispatchers.IO) ioDispatcher: CoroutineDispatcher
) : KVData() {

    init {
        FastKVConfig.setExecutor(ioDispatcher.asExecutor())
    }

    override val kv: FastKV by lazy {
        buildKV(context, name)
    }
}
