package com.imcys.bilibilias

import android.os.Bundle
import com.imcys.bilibilias.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //沉浸式
        statusBarOnly(this)

    }


}