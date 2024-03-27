package com.imcys.bilibilias.base

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceManager
import com.imcys.bilibilias.common.base.AbsActivity
import com.imcys.bilibilias.common.base.BaseActivityInit

abstract class BaseActivity : AbsActivity(), BaseActivityInit {

    override val asSharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarOnly(this)
        initView()
        initData()
        initObserveViewModel()
    }

    override fun initView() = Unit
    override fun initData() = Unit
    override fun initObserveViewModel() = Unit
}
