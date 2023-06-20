package com.imcys.bilibilias.base

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity

open class BaseComponentActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


}