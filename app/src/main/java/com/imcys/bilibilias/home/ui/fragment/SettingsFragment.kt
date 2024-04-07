package com.imcys.bilibilias.home.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.edit
import androidx.documentfile.provider.DocumentFile
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
import com.imcys.bilibilias.common.base.utils.file.isUriAuthorized
import com.imcys.bilibilias.home.ui.activity.SettingActivity


class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var userDownloadSaveSDPathSwitch: SwitchPreferenceCompat
    private lateinit var renameUserDownloadFileNameEditText: Preference
    lateinit var userDownloadSavePathEditText: Preference
    private lateinit var userDownloadFileNameEditText: Preference
    private lateinit var userDlFinishAutomaticMergeSwitch: SwitchPreferenceCompat
    private lateinit var userDlFinishAutomaticImportSwitch: SwitchPreferenceCompat
    private lateinit var appThemeListPreference: ListPreference
    private lateinit var appLanguageListPreference: ListPreference

    private lateinit var renameUserDownloadSavePath: Preference
    private val SAVE_FILE_PATH_CODE = 1
    private val IMPORT_FILE_PATH_CODE = 2

    private val TAG = this.javaClass.name

    private val saveImport = registerForActivityResult(
        ActivityResultContracts.OpenDocumentTree(),
    ) {
        (context as SettingActivity).asSharedPreferences.edit().apply {
            putString("AppDataUri", it.toString())
            putBoolean("user_dl_finish_automatic_import_switch", true)
            apply()
        }
    }

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        sharedPreferences = getDefaultSharedPreferences(requireContext())

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
                        "user_download_save_uri_path",
                        null,
                    )
                        .apply()
                    userDownloadSavePathEditText.summary =
                        "手机根目录：Android/data/com.imcys.bilibilias/files/download"
                },
                negativeButtonClickListener = {
                },
            ).show()
            true
        }

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
                        apply()
                    }
                    asToast(requireContext(), "恢复成功，返回页面重新进入可见")
                },
                negativeButtonClickListener = {
                },
            ).show()
            true
        }

        bindingGetSavePathEvent()

        bindingImportFileEvent()

        // SD卡也需要SAF授权，不再使用旧的方案
        // bindingSaveSDPathSwitchEvent()
        bindingAppThemeListPreferenceEvent()
        bindingAppLanguageListPreferenceEvent()
    }

    private fun bindingAppLanguageListPreferenceEvent() {
        appLanguageListPreference.onPreferenceChangeListener =
            OnPreferenceChangeListener { _, _ ->
                val intent = Intent("com.imcys.bilibilias.app.LANGUAGE_CHANGED")
                intent.setPackage(requireActivity().packageName)
                requireActivity().sendBroadcast(intent)
                true
            }
    }

    private fun bindingAppThemeListPreferenceEvent() {
        appThemeListPreference.onPreferenceChangeListener =
            OnPreferenceChangeListener { _, _ ->
                val intent = Intent("com.imcys.bilibilias.app.THEME_CHANGED")
                intent.setPackage(requireActivity().packageName)
                requireActivity().sendBroadcast(intent)
                true
            }
    }

    private fun bindingSaveSDPathSwitchEvent() {
        userDownloadSaveSDPathSwitch.setOnPreferenceClickListener {
            val sdPathState = getDefaultSharedPreferences(requireContext()).getBoolean(
                "user_download_save_sd_path_switch",
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

    @SuppressLint("UseRequireInsteadOfGet")
    private fun bindingImportFileEvent() {
        userDlFinishAutomaticImportSwitch.setOnPreferenceClickListener {
            val mUri = (context as SettingActivity).asSharedPreferences.getString("AppDataUri", "")
            // 判断是否有权限
            if (!Uri.parse(mUri).isUriAuthorized(requireContext())) {
                // 申请权限
                DialogUtils.dialog(
                    context!!,
                    "授权须知",
                    "简答来讲，这项功能需要你授权下文件的读写权限，BILIBILIAS仅仅会利用此权限实现导入番剧。",
                    "同意授权",
                    "拒绝授权",
                    true,
                    positiveButtonClickListener = {
//                         saveImport.launch(Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata"))
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

    private fun bindingGetSavePathEvent() {
        userDownloadSavePathEditText.setOnPreferenceClickListener {
//            fileUriUtils.startForRoot(
//                context as Activity,
//                SAVE_FILE_PATH_CODE,
//            )
            Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).also {
                requireActivity().startActivityForResult(it,SAVE_FILE_PATH_CODE)
            }
            true
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
        appThemeListPreference = findPreference("app_theme")!!
        appLanguageListPreference = findPreference("app_language")!!

        userDownloadSavePathEditText = findPreference("user_download_save_uri_path")!!

        val value = sharedPreferences.getString(
            "user_download_save_uri_path",
            null,
        )

        if (value == null) {
            userDownloadSavePathEditText.summary =
                "手机根目录:Android/data/com.imcys.bilibilias/files/download"
        } else {
            val documentFile = DocumentFile.fromSingleUri(requireContext(), Uri.parse(value))

            var filePath = documentFile!!.uri.path?.replace("/tree/primary:", "手机根目录:")
            filePath = filePath?.replace(Regex("/tree/.*:"), "储存卡:")

            userDownloadSavePathEditText.summary = filePath
        }

        renameUserDownloadSavePath = findPreference("rename_user_download_save_path")!!
        renameUserDownloadFileNameEditText =
            findPreference("rename_user_download_file_name_editText")!!
        // 暂停SD卡方案
        //        userDownloadSaveSDPathSwitch = findPreference("user_download_save_sd_path_switch")!!
        //
        //        val sdPathState = getDefaultSharedPreferences(requireContext()).getBoolean(
        //            "user_download_save_sd_path_switch",
        //            false,
        //        )
        //        // 禁止或者释放下载地址修改
        //        userDownloadSavePathEditText.isEnabled = !sdPathState
        //
        //        val tip = "应安卓要求，这里的路径无法修改，储存位置为:\n${
        //            AppFilePathUtils(
        //                App.context,
        //                "com.imcys.bilibilias",
        //            ).sdCardDirectory
        //        }/Android/data/com.imcys.bilibilias/files/download"
        //
        //        userDownloadSaveSDPathSwitch.summaryOn = tip
    }
}
