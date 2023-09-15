package com.imcys.bilibilias.view.base

import android.content.SharedPreferences
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.preference.PreferenceManager
import com.baidu.mobstat.StatService
import com.imcys.bilibilias.common.base.AbsActivity

abstract class BaseActivity<DB : ViewDataBinding> : AbsActivity(), BaseInit {

    val asSharedPreferences: SharedPreferences by lazy(LazyThreadSafetyMode.NONE) {
        PreferenceManager.getDefaultSharedPreferences(this)
    }

    @LayoutRes
    abstract fun getLayoutRes(): Int

    val binding by lazy {
        DataBindingUtil.setContentView(this, getLayoutRes()) as DB
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarOnly(this)
        initView()
        initData()
    }

    override fun initView() {}
    override fun initData() {}

    override fun onResume() {
        super.onResume()
        StatService.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        StatService.onPause(this)
    }
}
