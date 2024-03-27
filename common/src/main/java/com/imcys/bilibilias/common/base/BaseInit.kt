package com.imcys.bilibilias.common.base

interface BaseInit {
    fun initView()
    fun initData()
    fun initObserveViewModel()
}

interface BaseFragmentInit : BaseInit
