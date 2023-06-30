package com.imcys.bilibilias.home.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.baidu.mobstat.StatService
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.databinding.ActivitySttingBinding
import com.imcys.bilibilias.home.ui.fragment.SettingsFragment
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding

class SettingActivity : BaseActivity() {
    lateinit var binding: ActivitySttingBinding
    private val SAVE_FILE_PATH_CODE = 1
    private val IMPORT_FILE_PATH_CODE = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_stting)
        binding.settingTopLy.addStatusBarTopPadding()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.setting_FrameLayout, SettingsFragment())
            .commit()


    }


    @SuppressLint("WrongConstant")
    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        when (requestCode) {
            IMPORT_FILE_PATH_CODE -> {
                if (resultData != null) {
                    val takeFlags =
                        resultData.flags and (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    this.contentResolver.takePersistableUriPermission(
                        resultData.data!!,
                        takeFlags
                    )
                }

                asSharedPreferences.edit().apply {
                    putString("AppDataUri", resultData?.data!!.toString())
                    putBoolean("user_dl_finish_automatic_import_switch", true)
                    apply()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        StatService.onResume(this)
    }


    override fun onPause() {
        super.onPause()
        StatService.onPause(this)
    }
}