package com.imcys.bilibilias.base

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceManager
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.common.base.AbsActivity
import com.imcys.bilibilias.common.base.model.user.AsUser

open class BaseActivity : AbsActivity() {

     override val asSharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(this)
    }

    override val asUser: AsUser by lazy {
        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences("data", Context.MODE_PRIVATE)
        AsUser.apply {
            cookie = sharedPreferences.getString("cookies", "").toString()
            sessdata = sharedPreferences.getString("SESSDATA", "").toString()
            biliJct = sharedPreferences.getString("bili_jct", "").toString()
            mid = sharedPreferences.getLong("mid", 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.context = this

        // 沉浸式状态栏
        statusBarOnly(this)
    }


}
