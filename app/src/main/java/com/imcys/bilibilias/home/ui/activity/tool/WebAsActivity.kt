package com.imcys.bilibilias.home.ui.activity.tool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.databinding.ActivityWebAsBinding

class WebAsActivity : BaseActivity() {
    private lateinit var webAsBinding: ActivityWebAsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_as)
        //视图加载
        webAsBinding = DataBindingUtil.setContentView(this, R.layout.activity_web_as)
    }
}