package com.imcys.bilibilias.home.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity

class AsVideoActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_as_video)
        statusBarOnly(this)

    }
}