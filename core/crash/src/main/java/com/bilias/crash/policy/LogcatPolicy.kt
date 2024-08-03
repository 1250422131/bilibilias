package com.bilias.crash.policy

import android.util.Log
import com.bilias.crash.Info
import com.bilias.crash.group.DefaultGroup
import com.bilias.crash.group.Group

/**
 * 仅作测试API使用
 */
class LogcatPolicy(
    policy: CrashReportPolicy?,
    private val mLevel: Int = LOG_LEVEL_DEBUG,
    override val group: Group = DefaultGroup()
) : CrashReportPolicy(group, policy) {

    override fun report(info: Info, group: Group) {
        super.report(info, group)
        if (group.counts()) {
            when (mLevel) {
                LOG_LEVEL_DEBUG -> Log.d("bilias", info.toString())
                LOG_LEVEL_INFO -> Log.i("bilias", info.toString())
                LOG_LEVEL_ERROR -> Log.e("bilias", info.toString())
            }
        }
    }

    companion object {
        const val LOG_LEVEL_DEBUG = 0
        const val LOG_LEVEL_INFO = 1
        const val LOG_LEVEL_ERROR = 2
    }
}