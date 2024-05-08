package com.imcys.bilibilias.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.imcys.bilibilias.common.base.BaseActivityInit

abstract class BaseActivity<DB : ViewDataBinding> : AppCompatActivity(), BaseActivityInit {
    protected lateinit var binding: DB
        private set

    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<DB>(this, layoutId)
        initView()
        initData()
        initObserveViewModel()
    }

    override fun initView() = Unit
    override fun initData() = Unit
    override fun initObserveViewModel() = Unit
    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
}
