package com.imcys.bilibilias.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.baidu.mobstat.StatService
import com.imcys.bilibilias.common.base.AbsActivity
import com.imcys.bilibilias.common.base.BaseActivityInit

abstract class BaseActivity<DB : ViewDataBinding> : AbsActivity(), BaseActivityInit {
    protected lateinit var binding: DB
        private set

    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<DB>(this, layoutId)
        statusBarOnly(this)
        initView()
        initData()
        initObserveViewModel()
    }

    override fun initView() = Unit
    override fun initData() = Unit
    override fun initObserveViewModel() = Unit
    override fun onResume() {
        super.onResume()
        StatService.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        StatService.onPause(this)
    }
}
