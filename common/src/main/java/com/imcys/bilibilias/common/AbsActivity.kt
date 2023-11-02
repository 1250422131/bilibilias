package com.imcys.bilibilias.common

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.baidu.mobstat.StatService
import com.imcys.bilibilias.common.utils.asLogD
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

open class AbsActivity : AppCompatActivity() {

    // 存储所有活动的列表
    private val activities = mutableListOf<Activity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 打印活动名称
        asLogD(this, javaClass.simpleName)
        // 启动APP统计
        startAppCenter()
        // 添加当前活动
        addActivity(this)
        // 判断主题
        setTheme()
        // 判断语言
        setLanguage()
    }

    protected fun launchIO(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit,
    ) {
        lifecycleScope.launch(Dispatchers.IO, start, block)
    }

    protected fun launchUI(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit,
    ) {
        lifecycleScope.launch(Dispatchers.Main, start, block)
    }

    private fun setLanguage() {
        val configuration = Configuration()
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val locale = when (
            val language =
                sharedPreferences.getString("app_language", "System") ?: "System"
        ) {
            "System" -> Locale.getDefault()
            "Default" -> Locale("zh")
            else -> Locale(language.split("-")[0], language.split("-")[1])
        }
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 移除当前活动
        removeActivity(this)
    }

    /**
     * 启动统计
     */
    private fun startAppCenter() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        if (sharedPreferences.getBoolean("microsoft_app_center_type", false)) {
            if (!AppCenter.isConfigured()) {
                // 统计接入
                AppCenter.start(
                    application,
                    "3c7c5174-a6be-4093-a0df-c6fbf7371480",
                    Analytics::class.java,
                    Crashes::class.java,
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        StatService.onResume(this)
    }

    // 添加活动
    private fun addActivity(activity: Activity) {
        activities.add(activity)
    }

    // 移除活动
    private fun removeActivity(activity: Activity) {
        activities.remove(activity)
    }

    // 结束所有活动
    fun finishAll() {
        activities.forEach {
            if (!it.isFinishing) it.finish()
        }
    }

    // 沉浸式状态栏
    fun statusBarOnly(fragmentActivity: FragmentActivity) {
        UltimateBarX.statusBarOnly(fragmentActivity)
            .fitWindow(false)
            .light(true)
            .apply()
    }

    open fun updateTheme() {
        recreate()
    }

    // 检查主题
    private fun setTheme() {
        when (PreferenceManager.getDefaultSharedPreferences(this).getString("app_theme", "System")) {
            "System" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            "Light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "Dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "Pink" -> setTheme(R.style.Theme_BILIBILIAS)
            "Blue" -> setTheme(R.style.BILIBILIAS_BLUE)
        }
    }

    override fun onPause() {
        super.onPause()
        StatService.onPause(this)
    }
}
