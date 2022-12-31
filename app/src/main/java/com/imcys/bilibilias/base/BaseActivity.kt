package com.imcys.bilibilias.base

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import com.imcys.bilibilias.base.utils.asLogD
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX

open class BaseActivity : AppCompatActivity() {

    // 活动的类名
    private val TAG = javaClass.simpleName

    // 存储所有活动的列表
    private val activities = mutableListOf<Activity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        if (sharedPreferences.getBoolean("microsoft_app_center_type", true)) {
            //统计接入
            AppCenter.start(application,
                "3c7c5174-a6be-4093-a0df-c6fbf7371480",
                Analytics::class.java,
                Crashes::class.java)

        }


        // 添加当前活动
        addActivity(this)
        // 打印活动名称
        asLogD(this, TAG)
        // 沉浸式状态栏
        statusBarOnly(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 移除当前活动
        removeActivity(this)
    }

    // 沉浸式状态栏
    fun statusBarOnly(fragmentActivity: FragmentActivity) {
        UltimateBarX.statusBarOnly(fragmentActivity)
            .fitWindow(false)
            .light(true)
            .apply()
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

}
