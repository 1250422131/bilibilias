package com.imcys.bilibilias.home.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.utils.file.AppFilePathUtils
import com.imcys.bilibilias.utils.file.fileUriUtils
import me.rosuh.filepicker.bean.FileItemBeanImpl
import me.rosuh.filepicker.config.AbstractFileFilter
import me.rosuh.filepicker.config.FilePickerManager


class SettingsFragment : PreferenceFragmentCompat() {


    private lateinit var userDownloadSavePathEditText: Preference
    private lateinit var userDownloadFileNameEditText: Preference
    private lateinit var userDlFinishAutomaticMergeSwitch: Preference
    private lateinit var userDlFinishAutomaticImportSwitch: Preference
    private val SAVE_FILE_PATH_CODE = 1


    private val saveImport = registerForActivityResult(
        ActivityResultContracts.OpenDocumentTree()) {
        App.sharedPreferences.edit().apply {
            putString("AppDataUri", it.toString())
            putBoolean("user_dl_finish_automatic_import_switch", true)
            apply()
        }
    }


    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        sharedPreferences = getDefaultSharedPreferences(context)

        initPreference()

        bindingEvent()

    }

    private fun bindingEvent() {
        userDownloadFileNameEditText.setOnPreferenceChangeListener { preference, newValue ->

            val regex = Regex(""".*([<>:*?"]).*""")
            if (newValue.toString() in "{FILE_TYPE}") {
                Toast.makeText(context, "缺少文件后缀", Toast.LENGTH_SHORT).show()
                false
            } else {
                (!regex.containsMatchIn(newValue.toString())).apply {
                    if (!this) Toast.makeText(context, "存在特殊符号", Toast.LENGTH_SHORT).show()
                }
            }

        }
        bindingGetSavePathEvent()
        bindingImportFileEvent()

    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun bindingImportFileEvent() {
        userDlFinishAutomaticImportSwitch.setOnPreferenceClickListener {
            //判断是否有权限
            if (!fileUriUtils.isGrant(context)) {
                //申请权限
                DialogUtils.dialog(
                    context!!,
                    "授权须知",
                    "简答来讲，这项功能需要你授权下文件的读写权限，BILIBILIAS仅仅会利用此权限实现导入番剧。",
                    "同意授权",
                    "拒绝授权",
                    {
                        saveImport.launch(Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata"))
                    },
                    {
                    }
                ).show()
                false
            } else {
                true
            }


        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun bindingGetSavePathEvent() {
        userDownloadSavePathEditText.setOnPreferenceClickListener {
            DialogUtils.dialog(context!!, "选择区域",
                "现在选择，你要在手机内部储存还是SD卡呢？",
                "手机内部储存",
                "SD卡", {
                    FilePickerManager
                        .from(this)
                        .maxSelectable(1)
                        .filter(object : AbstractFileFilter() {
                            override fun doFilter(listData: ArrayList<FileItemBeanImpl>): ArrayList<FileItemBeanImpl> {
                                return ArrayList(listData.filter { item ->
                                    item.isDir
                                })
                            }
                        }).skipDirWhenSelect(false)
                        .forResult(SAVE_FILE_PATH_CODE)

                }, {
                    FilePickerManager
                        .from(this)
                        .maxSelectable(1)
                        .filter(object : AbstractFileFilter() {
                            override fun doFilter(listData: ArrayList<FileItemBeanImpl>): ArrayList<FileItemBeanImpl> {
                                return ArrayList(listData.filter { item ->
                                    item.isDir
                                })
                            }
                        }).skipDirWhenSelect(false)
                        .setCustomRootPath(AppFilePathUtils(
                            context,
                            "com.imcys.bilibilias").sdCardDirectory)
                        .forResult(SAVE_FILE_PATH_CODE)
                }
            ).show()



            false
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            SAVE_FILE_PATH_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val list = FilePickerManager.obtainData().forEach {
                        sharedPreferences.edit().apply {
                            putString("user_download_save_path", it)
                            apply()
                            userDownloadSavePathEditText.summary = it
                        }
                    }
                }
            }
        }

    }


    /**
     * 初始化控件
     */
    private fun initPreference() {

        userDownloadFileNameEditText = findPreference("user_download_file_name_editText")!!
        userDlFinishAutomaticMergeSwitch = findPreference("user_dl_finish_automatic_merge_switch")!!
        userDlFinishAutomaticImportSwitch =
            findPreference("user_dl_finish_automatic_import_switch")!!

        userDownloadSavePathEditText = findPreference("user_download_save_path")!!
        sharedPreferences.apply {
            val value = getString("user_download_save_path",
                "/storage/emulated/0/Android/data/com.imcys.bilibilias/files/download")
            userDownloadSavePathEditText.summary = value


        }

    }


}