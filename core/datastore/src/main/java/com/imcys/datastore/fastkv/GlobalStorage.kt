package com.imcys.datastore.fastkv

import android.content.Context
import io.fastkv.FastKV

open class GlobalStorage(context: Context, name: String) : KVData() {
    override val kv: FastKV by lazy {
        buildKV(context, name)
    }
}