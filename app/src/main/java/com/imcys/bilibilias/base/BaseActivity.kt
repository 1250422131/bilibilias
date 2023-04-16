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
        PreferenceManager.getDefaultSharedPreferences(applicationContext)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 沉浸式状态栏
        statusBarOnly(this)
    }


}
