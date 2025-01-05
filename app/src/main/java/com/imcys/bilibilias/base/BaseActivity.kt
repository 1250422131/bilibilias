package com.imcys.bilibilias.base

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.imcys.bilibilias.common.base.AbsActivity

open class BaseActivity : AbsActivity() {

    override val asSharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 沉浸式状态栏
        statusBarOnly(this)
        initData()
    }

    open fun initData() {}
    fun toast(text: String, duration: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this, text, duration).show()

    fun toast(resId: Int, duration: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this, resId, duration).show()
}
