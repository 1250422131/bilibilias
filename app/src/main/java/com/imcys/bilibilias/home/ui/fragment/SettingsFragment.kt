package com.imcys.bilibilias.home.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.edit
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.Preference.OnPreferenceChangeListener
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.preference.SwitchPreferenceCompat
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.common.base.utils.asToast
import com.imcys.bilibilias.common.base.utils.file.AppFilePathUtils
import com.imcys.bilibilias.common.base.utils.file.fileUriUtils
import com.imcys.bilibilias.home.ui.activity.SettingActivity
import me.rosuh.filepicker.bean.FileItemBeanImpl
import me.rosuh.filepicker.config.AbstractFileFilter
import me.rosuh.filepicker.config.FilePickerManager

private const val user_download_save_sd_path_switch = "user_download_save_sd_path_switch"

private const val KEY_USER_DOWNLOAD_SAVE_PATH = "user_download_save_path"

private const val download_path = "/storage/emulated/0/Android/data/com.imcys.bilibilias/files/download"

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var userDownloadSaveSDPathSwitch: SwitchPreferenceCompat
    private lateinit var renameUserDownloadFileNameEditText: Preference
    private lateinit var userDownloadSavePathEditText: Preference
    private lateinit var userDownloadFileNameEditText: Preference
    private lateinit var userDlFinishAutomaticMergeSwitch: SwitchPreferenceCompat
    private lateinit var userDlFinishAutomaticImportSwitch: SwitchPreferenceCompat
    private lateinit var appThemeListPreference: ListPreference
    private lateinit var appLanguageListPreference: ListPreference

    private lateinit var renameUserDownloadSavePath: Preference
    private val SAVE_FILE_PATH_CODE = 1
    private val IMPORT_FILE_PATH_CODE = 2

    private val saveImport = registerForActivityResult(
        ActivityResultContracts.OpenDocumentTree(),
    ) {
        (context as SettingActivity).asSharedPreferences.edit {
            putString("AppDataUri", it.toString())
            putBoolean("user_dl_finish_automatic_import_switch", true)
        }
    }

    private val saveSDFile =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                setSavePath()
            }
        }

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        sharedPreferences = getDefaultSharedPreferences(requireContext())

        initUserDownloadSaveSDPathSwitch()
        initRenameUserDownloadFileNameEditText()
        initUserDownloadSavePathEditText()
        initUserDownloadFileNamePreferences()
        initUserDlFinishAutomaticMergeSwitch()
        initUserDlFinishAutomaticImportSwitch()
        initAppThemeListPreference()
        initAppLanguageListPreference()
        initRenameUserDownloadSavePath()
        
        initPreference()
    }

    private fun initRenameUserDownloadSavePath() {
        renameUserDownloadSavePath = findPreference("rename_user_download_save_path")!!
        renameUserDownloadSavePath.setOnPreferenceClickListener {
            DialogUtils.dialog(
                requireContext(),
                "警告",
                "是否还原默认下载地址",
                "是",
                "否",
                true,
                positiveButtonClickListener = {
                    getDefaultSharedPreferences(requireContext()).edit().putString(
                        KEY_USER_DOWNLOAD_SAVE_PATH,
                        download_path,
                    )
                        .apply()
                    asToast(requireContext(), "恢复成功，返回页面重新进入可见")
                    userDownloadSavePathEditText.summary =
                        download_path
                },
                negativeButtonClickListener = {
                },
            ).show()
            true
        }
    }

    private fun initAppLanguageListPreference() {
        appLanguageListPreference = findPreference("app_language")!!
        appLanguageListPreference.onPreferenceChangeListener =
            OnPreferenceChangeListener { _, _ ->
                val intent = Intent("com.imcys.bilibilias.app.LANGUAGE_CHANGED")
                intent.setPackage(requireActivity().packageName)
                requireActivity().sendBroadcast(intent)
                true
            }
    }

    private fun initAppThemeListPreference() {
        appThemeListPreference = findPreference("app_theme")!!
        appThemeListPreference.onPreferenceChangeListener =
            OnPreferenceChangeListener { _, _ ->
                val intent = Intent("com.imcys.bilibilias.app.THEME_CHANGED")
                intent.setPackage(requireActivity().packageName)
                requireActivity().sendBroadcast(intent)
                true
            }
    }

    private fun initUserDlFinishAutomaticImportSwitch() {
        userDlFinishAutomaticImportSwitch =
            findPreference("user_dl_finish_automatic_import_switch")!!
        userDlFinishAutomaticImportSwitch.setOnPreferenceClickListener {
            // 判断是否有权限
            if (!fileUriUtils.isGrant(context)) {
                // 申请权限
                DialogUtils.dialog(
                    requireContext(),
                    "授权须知",
                    "简答来讲，这项功能需要你授权下文件的读写权限，BILIBILIAS仅仅会利用此权限实现导入番剧。",
                    "同意授权",
                    "拒绝授权",
                    true,
                    positiveButtonClickListener = {
                        // saveImport.launch(Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata"))
                        fileUriUtils.startFor(
                            "/storage/emulated/0/Android/data/tv.danmaku.bili",
                            context as Activity,
                            IMPORT_FILE_PATH_CODE,
                        )
                    },
                    negativeButtonClickListener = {
                    },
                ).show()
                false
            } else {
                true
            }
        }
    }

    private fun initUserDlFinishAutomaticMergeSwitch() {
        userDlFinishAutomaticMergeSwitch = findPreference("user_dl_finish_automatic_merge_switch")!!
    }

    private fun initUserDownloadSavePathEditText() {
        userDownloadSavePathEditText = findPreference(KEY_USER_DOWNLOAD_SAVE_PATH)!!
        userDownloadSavePathEditText.setOnPreferenceClickListener {
            // Android 11 (Api 30)或更高版本的写文件权限需要特殊申请，需要动态申请管理所有文件的权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    setSavePath()
                    true
                } else {
                    saveSDFile.launch(Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION))
                    false
                }
            } else {
                // 小于安卓11
                setSavePath()
                false
            }
        }
    }

    private fun initRenameUserDownloadFileNameEditText() {
        renameUserDownloadFileNameEditText =
            findPreference("rename_user_download_file_name_editText")!!

        renameUserDownloadFileNameEditText.setOnPreferenceClickListener {
            DialogUtils.dialog(
                requireContext(),
                "警告",
                "是否还原默认命名规则",
                "是",
                "否",
                true,
                positiveButtonClickListener = {
                    getDefaultSharedPreferences(requireContext()).edit {
                        putString(
                            "user_download_file_name_editText",
                            "{BV}/{FILE_TYPE}/{P_TITLE}_{CID}.{FILE_TYPE}",
                        )
                    }
                    asToast(requireContext(), "恢复成功，返回页面重新进入可见")
                },
                negativeButtonClickListener = {
                },
            ).show()
            true
        }
    }

    private fun initUserDownloadSaveSDPathSwitch() {
        userDownloadSaveSDPathSwitch = findPreference(user_download_save_sd_path_switch)!!
        userDownloadSaveSDPathSwitch.setOnPreferenceClickListener {
            val sdPathState = getDefaultSharedPreferences(requireContext()).getBoolean(
                user_download_save_sd_path_switch,
                false,
            )
            // 禁止或者释放下载地址修改
            userDownloadSavePathEditText.isEnabled = !sdPathState

            val tip = "这里的路径无法修改，储存位置为:\n${
                AppFilePathUtils(
                    App.context,
                    "com.imcys.bilibilias",
                ).sdCardDirectory
            }/Android/data/com.imcys.bilibilias/files/download"

            userDownloadSaveSDPathSwitch.summaryOn = tip

            true
        }
    }

    private fun initUserDownloadFileNamePreferences() {
        userDownloadFileNameEditText = findPreference("user_download_file_name_editText")!!
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
    }
    private fun setSavePath() {
        FilePickerManager
            .from(this)
            .maxSelectable(1)
            .filter(object : AbstractFileFilter() {
                override fun doFilter(listData: ArrayList<FileItemBeanImpl>): ArrayList<FileItemBeanImpl> {
                    return ArrayList(
                        listData.filter { item ->
                            item.isDir
                        },
                    )
                }
            }).skipDirWhenSelect(false)
            .forResult(SAVE_FILE_PATH_CODE)
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("WrongConstant", "UseRequireInsteadOfGet")
    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        when (requestCode) {
            SAVE_FILE_PATH_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    FilePickerManager.obtainData().forEach {
                        sharedPreferences.edit {
                            putString(KEY_USER_DOWNLOAD_SAVE_PATH, it)
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
        sharedPreferences.apply {
            val value = getString(
                KEY_USER_DOWNLOAD_SAVE_PATH,
                download_path,
            )
            userDownloadSavePathEditText.summary = value
        }

        val sdPathState = getDefaultSharedPreferences(requireContext()).getBoolean(
            user_download_save_sd_path_switch,
            false,
        )
        // 禁止或者释放下载地址修改
        userDownloadSavePathEditText.isEnabled = !sdPathState

        val tip = "应安卓要求，这里的路径无法修改，储存位置为:\n${
            AppFilePathUtils(
                App.context,
                "com.imcys.bilibilias",
            ).sdCardDirectory
        }/Android/data/com.imcys.bilibilias/files/download"

        userDownloadSaveSDPathSwitch.summaryOn = tip
    }
}
