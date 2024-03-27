package com.imcys.bilibilias

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX.statusBarOnly

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarOnly(this)
    }
}
