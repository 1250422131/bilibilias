package com.imcys.bilibilias.common.base

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import com.imcys.bilibilias.base.utils.asLogD
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.model.user.AsUser
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.microsoft.appcenter.distribute.Distribute
import com.microsoft.appcenter.distribute.UpdateTrack
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX


open class AbsActivity : AppCompatActivity() {

    open val asSharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(this)
    }

    open val asUser: AsUser by lazy {
        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences("data", Context.MODE_PRIVATE)
        AsUser.apply {
            cookie = sharedPreferences.getString("cookies", "").toString()
            sessdata = sharedPreferences.getString("SESSDATA", "").toString()
            biliJct = sharedPreferences.getString("bili_jct", "").toString()
            mid = sharedPreferences.getLong("mid", 0)
        }
    }


    // 存储所有活动的列表
    private val activities = mutableListOf<Activity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 打印活动名称
        asLogD(this, javaClass.simpleName)
        //启动APP统计
        startAppCenter()
        // 添加当前活动
        BaseApplication.context = this
        addActivity(this)
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
                //统计接入
                Distribute.setEnabledForDebuggableBuild(true)

                AppCenter.start(
                    application,
                    BaseApplication.appSecret,
                    Analytics::class.java,
                    Crashes::class.java,
                    Distribute::class.java
                )
                Distribute.setUpdateTrack(UpdateTrack.PUBLIC)

            }

        }

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


}