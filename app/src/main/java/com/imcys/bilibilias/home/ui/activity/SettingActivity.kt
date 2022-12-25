package com.imcys.bilibilias.home.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.databinding.ActivitySttingBinding
import com.imcys.bilibilias.home.ui.fragment.SettingsFragment
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding

class SettingActivity : BaseActivity() {
    lateinit var binding: ActivitySttingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_stting)
        binding.settingTopLy.addStatusBarTopPadding()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.setting_FrameLayout, SettingsFragment())
            .commit()


    }
}