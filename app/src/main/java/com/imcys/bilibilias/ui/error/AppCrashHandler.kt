package com.imcys.bilibilias.ui.error

import android.content.Context
import android.content.Intent
import android.os.Process.*
import kotlin.system.exitProcess

class AppCrashHandler private constructor() : Thread.UncaughtExceptionHandler {

    private lateinit var context: Context

    private val defaultSystemExpHandler = Thread.getDefaultUncaughtExceptionHandler()

    fun init(context: Context) {
        this.context = context
        Thread.setDefaultUncaughtExceptionHandler(this)
    }


    override fun uncaughtException(t: Thread, error: Throwable) {
        context.startActivity(
            Intent(context, AppCrashActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                putExtra("appErrorMsg", error.toString())
            }
        )
        killProcess(myPid())
        exitProcess(1)
    }


    companion object {
        private const val TAG = "AppCrashHandler"
        val instance: AppCrashHandler by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            AppCrashHandler()
        }
    }

}