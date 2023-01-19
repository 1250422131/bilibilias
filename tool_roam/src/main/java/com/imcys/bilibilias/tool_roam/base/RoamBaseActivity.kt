package com.imcys.bilibilias.tool_roam.base

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.imcys.bilibilias.common.base.AbsActivity
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX


open class RoamBaseActivity : AbsActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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