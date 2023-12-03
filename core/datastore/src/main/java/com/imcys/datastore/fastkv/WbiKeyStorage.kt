package com.imcys.datastore.fastkv

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.todayIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WbiKeyStorage
@Inject constructor(@ApplicationContext context: Context) : FastKVOwner("cookies", context) {

    private var recordLocalDate: String? by string()

    var mixKey: String? by string()
        private set

    fun updateLocalDate() {
        if (shouldNeedUpdate()) {
            recordLocalDate = today().toString()
        }
    }

    fun save(mixKey: String) {
        this.mixKey = mixKey
    }

    /**
     * 是否需要更新
     */
    fun shouldNeedUpdate(): Boolean {
        if (recordLocalDate.isNullOrBlank()) {
            return true
        }
        return recordLocalDate?.toLocalDate() != today()
    }

    private fun today() = Clock.System.todayIn(TimeZone.currentSystemDefault())
}