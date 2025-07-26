package com.imcys.bilibilias.common.base.crash

import android.content.Context
import com.imcys.bilibilias.common.event.sendAppErrorEvent
import com.imcys.bilibilias.common.event.sendLoginErrorEvent

class AppCrashHandler private constructor() : Thread.UncaughtExceptionHandler {

    private lateinit var context: Context

    private val defaultSystemExpHandler = Thread.getDefaultUncaughtExceptionHandler()

    fun init(context: Context) {
        this.context = context
        Thread.setDefaultUncaughtExceptionHandler(this)
    }


    override fun uncaughtException(t: Thread, error: Throwable) {
        if (error is AppException) {
            sendAppErrorEvent(error)
        } else {
            // 处理其他异常
            defaultSystemExpHandler?.uncaughtException(t,error)
        }
    }


    companion object {
        private const val TAG = "AppCrashHandler"
        val instance: AppCrashHandler by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            AppCrashHandler()
        }
    }

}