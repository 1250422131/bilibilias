package com.imcys.datastore.fastkv

import android.content.*
import dagger.hilt.android.qualifiers.*
import io.github.aakira.napier.*
import kotlinx.datetime.*
import javax.inject.*

@Singleton
class WbiKeyStorage @Inject constructor(
    @ApplicationContext context: Context
) : FastKVOwner("wbi", context) {
    // 上次修改时间
    private var lastModified: String by string("WbiKeyStorage-lastModified")

    init {
        lastModified = today()
    }

    var mixKey: String by string("WbiKeyStorage-mixKey")
        private set

    fun save(mixKey: String) {
        this.mixKey = mixKey
        Napier.d { "wbi mixKey: $mixKey" }
    }

    private fun today() = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
}