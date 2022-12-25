package com.imcys.bilibilias.home.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.imcys.bilibilias.R


class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var userDownloadFileNameEditText: Preference
    private lateinit var userDlFinishAutomaticMergeSwitch: Preference
    private lateinit var userDlFinishAutomaticImportSwitch: Preference
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
                Toast.makeText(context, "存在特殊符号", Toast.LENGTH_SHORT).show()
                !regex.containsMatchIn(newValue.toString())
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

    }


}