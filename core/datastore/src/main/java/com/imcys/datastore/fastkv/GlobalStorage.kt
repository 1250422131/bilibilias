package com.imcys.datastore.fastkv

import dev.*
import io.fastkv.*
import javax.inject.*

open class GlobalStorage @Inject constructor(name: String) : KVData() {


    override val kv: FastKV by lazy {
        buildKV(DevUtils.getContext(), name)
    }
}
