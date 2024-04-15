package com.imcys.bilibilias.home.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.documentfile.provider.DocumentFile
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.Preference.OnPreferenceChangeListener
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.hjq.toast.Toaster
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.common.base.utils.file.FileUriUtils
import com.imcys.bilibilias.common.base.utils.file.isUriAuthorized
import com.imcys.bilibilias.core.common.utils.getBiliBiliUri
import com.imcys.bilibilias.core.common.utils.get下载路径
import com.imcys.bilibilias.core.common.utils.restoreDownloadAddress
import com.imcys.bilibilias.core.common.utils.restoreVideoNameRule
import com.imcys.bilibilias.core.common.utils.setUserDownloadFileNameRule
import com.imcys.bilibilias.core.common.utils.微软统计
import com.imcys.bilibilias.core.common.utils.百度统计
import com.imcys.bilibilias.core.network.download.downloadDir
import dev.utils.app.AppUtils

private const val SAVE_FILE_PATH_CODE = 1
private const val IMPORT_FILE_PATH_CODE = 2
private const val 保存路径 = "user_download_save_uri_path"
private const val 命名规则 = "user_download_file_name_editText"
private const val 还原下载路径 = "rename_user_download_save_path"
private const val 还原命名规则 = "rename_user_download_file_name_editText"
private const val 自动合并 = "user_dl_finish_automatic_merge_switch"
private const val 导入B站 = "user_dl_finish_automatic_import_switch"
private const val 合并视频后删除音视频文件 = "user_dl_finish_delete_merge_switch"
private const val 合并命令 = "user_dl_merge_cmd_editText"
private const val 语言 = "app_language"
private const val 主题 = "app_theme"
internal const val TAG_LANGUAGE_CHANGED = "com.imcys.bilibilias.app.LANGUAGE_CHANGED"
internal const val TAG_THEME_CHANGED = "com.imcys.bilibilias.app.THEME_CHANGED"

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        保存路径()
        命名规则()
        还原下载路径()
        还原命名规则()
        自动合并()
        主题()
        语言()
        统计数据()
    }

    private fun 统计数据() {
        findPreference<SwitchPreferenceCompat>("microsoft_app_center_type")!!.setOnPreferenceChangeListener { preference, newValue ->
            微软统计(newValue as Boolean)
            true
        }
        findPreference<SwitchPreferenceCompat>("baidu_statistics_type")!!.setOnPreferenceChangeListener { preference, newValue ->
            百度统计(newValue as Boolean)
            true
        }
    }

    private fun 命名规则() {
        findPreference<EditTextPreference>(命名规则)!!.setOnBindEditTextListener {
            setUserDownloadFileNameRule(it.text.toString())
        }
    }

    private fun 保存路径() {
        findPreference<Preference>(保存路径)!!.apply {
            setOnPreferenceClickListener {
                Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).also {
                    requireActivity().startActivityForResult(it, SAVE_FILE_PATH_CODE)
                }
                true
            }
        }

        val savePath = get下载路径()

        val path = if (savePath == null) {
            requireContext().downloadDir.absolutePath
        } else {
            DocumentFile.fromSingleUri(requireContext(), Uri.parse(savePath))!!.uri.path!!
        }
        更新保存路径(path)
    }

    fun 更新保存路径(text: String) {
        findPreference<Preference>(保存路径)!!.summary = text
    }

    private fun 导入B站() {
        findPreference<SwitchPreferenceCompat>(导入B站)!!
            .setOnPreferenceClickListener {
                val mUri = getBiliBiliUri()
                // 判断是否有权限
                if (!Uri.parse(mUri).isUriAuthorized(requireContext())) {
                    // 申请权限
                    DialogUtils.dialog(
                        requireContext(),
                        "授权须知",
                        "简答来讲，这项功能需要你授权下文件的读写权限，BILIBILIAS仅仅会利用此权限实现导入番剧。",
                        "同意授权",
                        "拒绝授权",
                        true,
                        positiveButtonClickListener = {
                            FileUriUtils.startFor(
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

    private fun 自动合并() {
        findPreference<SwitchPreferenceCompat>(自动合并)!!
    }

    private fun 还原下载路径() {
        findPreference<Preference>(还原下载路径)!!.setOnPreferenceClickListener {
            DialogUtils.dialog(
                requireContext(),
                "警告",
                "是否还原默认下载地址",
                "是",
                "否",
                true,
                positiveButtonClickListener = {
                    restoreDownloadAddress()
                    更新保存路径(requireContext().downloadDir.absolutePath)
                },
                negativeButtonClickListener = {
                },
            ).show()
            true
        }
    }

    private fun 还原命名规则() {
        findPreference<Preference>(还原命名规则)!!.setOnPreferenceClickListener {
            DialogUtils.dialog(
                requireContext(),
                "警告",
                "是否还原默认命名规则",
                "是",
                "否",
                true,
                positiveButtonClickListener = {
                    restoreVideoNameRule()
                    Toaster.show("恢复成功，返回页面重新进入可见")
                },
                negativeButtonClickListener = {
                },
            ).show()
            true
        }
    }

    private fun 语言() {
        findPreference<ListPreference>(语言)!!.onPreferenceChangeListener =
            OnPreferenceChangeListener { _, _ ->
                val intent = Intent(TAG_LANGUAGE_CHANGED)
                intent.setPackage(requireActivity().packageName)
                AppUtils.sendBroadcast(intent)
                true
            }
    }

    private fun 主题() {
        findPreference<ListPreference>(主题)!!.onPreferenceChangeListener =
            OnPreferenceChangeListener { _, _ ->
                val intent = Intent(TAG_THEME_CHANGED)
                intent.setPackage(requireActivity().packageName)
                AppUtils.sendBroadcast(intent)
                true
            }
    }
}
