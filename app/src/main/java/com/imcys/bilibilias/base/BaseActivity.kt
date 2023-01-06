package com.imcys.bilibilias.base

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.base.utils.asLogD
import com.imcys.bilibilias.common.base.AbsActivity
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX

open class BaseActivity : AbsActivity() {

    // 活动的类名
    private val TAG = javaClass.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.context = this

        // 打印活动名称
        asLogD(this, TAG)

        // 沉浸式状态栏
        statusBarOnly(this)
    }



    // 沉浸式状态栏
    fun statusBarOnly(fragmentActivity: FragmentActivity) {
        UltimateBarX.statusBarOnly(fragmentActivity)
            .fitWindow(false)
            .light(true)
            .apply()
    }


}
