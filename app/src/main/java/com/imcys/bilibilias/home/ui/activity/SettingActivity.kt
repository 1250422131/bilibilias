package com.imcys.bilibilias.home.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.documentfile.provider.DocumentFile
import com.baidu.mobstat.StatService
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.common.base.utils.file.hasSubDirectory
import com.imcys.bilibilias.databinding.ActivitySttingBinding
import com.imcys.bilibilias.home.ui.fragment.SettingsFragment
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding

class SettingActivity : BaseActivity() {
    private lateinit var settingsFragment: SettingsFragment
    lateinit var binding: ActivitySttingBinding
    private val SAVE_FILE_PATH_CODE = 1
    private val IMPORT_FILE_PATH_CODE = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_stting)
        binding.settingTopLy.addStatusBarTopPadding()
        settingsFragment = SettingsFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.setting_FrameLayout, settingsFragment)
            .commit()


    }


    @Deprecated("Deprecated in Java")
    @SuppressLint("WrongConstant")
    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        when (requestCode) {
            // 对导入授权
            IMPORT_FILE_PATH_CODE -> {
                if (resultData != null) {
                    val takeFlags =
                        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    this.contentResolver.takePersistableUriPermission(
                        resultData.data!!,
                        takeFlags
                    )
                    asSharedPreferences.edit {
                        putString("AppDataUri", resultData.data!!.toString())
                        putBoolean("user_dl_finish_automatic_import_switch", true)
                    }
                    resultData.data?.also {
                        val mDocumentFile = DocumentFile.fromTreeUri(this, it)
                        if (!mDocumentFile!!.hasSubDirectory( "download")) {
                            mDocumentFile.createDirectory("download")
                        }
                    }

                }
            }
            // 对下载地址授权
            SAVE_FILE_PATH_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val takeFlags =
                        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    this.contentResolver.takePersistableUriPermission(
                        resultData?.data!!,
                        takeFlags
                    )
                    asSharedPreferences.edit {
                        putString("user_download_save_uri_path", resultData.data.toString())
                    }
                    val documentFile = DocumentFile.fromSingleUri(this, resultData.data!!)

                    // Not need to translate to file path
                    var filePath = documentFile!!.uri.path?.replace("/tree/primary:", "手机根目录:")
                    filePath = filePath?.replace(Regex("/tree/.*:"), "储存卡:")
                    settingsFragment.userDownloadSavePathEditText.summary = filePath
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