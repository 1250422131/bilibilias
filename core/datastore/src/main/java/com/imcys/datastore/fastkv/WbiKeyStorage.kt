package com.imcys.datastore.fastkv

import android.content.*
import dagger.hilt.android.qualifiers.*
import kotlinx.datetime.*
import javax.inject.*

@Singleton
class WbiKeyStorage @Inject constructor(
    @ApplicationContext context: Context
) : FastKVOwner("wbi", context) {

    private var recordLocalDate: String? by string()

    var mixKey: String? by string()
        private set

    fun updateLocalDate() {
        recordLocalDate = today()
    }

    fun save(mixKey: String) {
        if (mixKey.isNotBlank()) {
            this.mixKey = mixKey
        }
    }

    /** 是否需要更新 */
    fun shouldUpdate(): Boolean {
        if (recordLocalDate.isNullOrBlank()) {
            return true
        }
        return recordLocalDate != today()
    }

    private fun today() = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
}