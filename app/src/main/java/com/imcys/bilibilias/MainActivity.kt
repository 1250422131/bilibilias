package com.imcys.bilibilias

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.utils.HttpUtils
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX

class MainActivity : BaseActivity() {

    private val TAG = HttpUtils::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //沉浸式
        statusBarOnly(this)


    }



}