package com.imcys.bilibilias.home.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.core.content.edit
import com.imcys.bilibilias.R
import com.imcys.bilibilias.databinding.ActivitySttingBinding
import com.imcys.bilibilias.home.ui.fragment.SettingsFragment
import com.imcys.bilibilias.view.base.BaseActivity
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding

class SettingActivity : BaseActivity<ActivitySttingBinding>() {

    private val SAVE_FILE_PATH_CODE = 1
    private val IMPORT_FILE_PATH_CODE = 2
    override fun getLayoutRes(): Int = R.layout.activity_stting

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.settingTopLy.addStatusBarTopPadding()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.setting_FrameLayout, SettingsFragment())
            .commit()
    }

    @Deprecated("Deprecated in Java")
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
                asSharedPreferences.edit {
                    putString("AppDataUri", resultData?.data!!.toString())
                    putBoolean("user_dl_finish_automatic_import_switch", true)
                }
            }
        }
    }
}
