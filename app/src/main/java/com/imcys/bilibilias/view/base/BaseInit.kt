package com.imcys.bilibilias.view.base

import android.view.View
import androidx.annotation.LayoutRes

/**
 * 在Activity或Fragment中初始化需要的函数。
 *
 */
interface BaseInit {
    @get:LayoutRes
    val layoutId: Int
    fun initData()

    fun initView()
}

interface BaseActivityInit : BaseInit {

    fun getLayoutView(): View
}

interface BaseFragmentInit : BaseInit
