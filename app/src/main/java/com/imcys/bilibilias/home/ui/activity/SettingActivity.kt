package com.imcys.bilibilias.home.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.documentfile.provider.DocumentFile
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.core.common.utils.setBiliBiliUri
import com.imcys.bilibilias.core.common.utils.set保存路径
import com.imcys.bilibilias.databinding.ActivitySttingBinding
import com.imcys.bilibilias.home.ui.fragment.SettingsFragment
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding

private const val SAVE_FILE_PATH_CODE = 1
private const val IMPORT_FILE_PATH_CODE = 2

class SettingActivity : BaseActivity<ActivitySttingBinding>() {
    override val layoutId: Int = R.layout.activity_stting

    private val settingsFragment: SettingsFragment = SettingsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.settingTopLy.addStatusBarTopPadding()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.setting_FrameLayout, settingsFragment)
            .commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        when (requestCode) {
            // 对导入授权
            IMPORT_FILE_PATH_CODE -> {
                if (resultCode == Activity.RESULT_OK && resultData != null) {
                    requestPermission(resultData.data!!)
                    setBiliBiliUri(resultData.data!!.toString())
                }
            }
            // 对下载地址授权
            SAVE_FILE_PATH_CODE -> {
                if (resultCode == Activity.RESULT_OK && resultData != null) {
                    requestPermission(resultData.data!!)
                    set保存路径(resultData.data.toString())
                    val documentFile = DocumentFile.fromSingleUri(this, resultData.data!!)
                    settingsFragment.更新保存路径(documentFile!!.uri.path!!)
                }
            }
        }
    }
    private fun requestPermission(data: Uri) {
        contentResolver.takePersistableUriPermission(
            data,
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
    }
}
