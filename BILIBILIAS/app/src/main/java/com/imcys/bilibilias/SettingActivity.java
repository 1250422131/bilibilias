package com.imcys.bilibilias;


import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;


import android.provider.DocumentsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.documentfile.provider.DocumentFile;

import com.baidu.mobstat.StatService;
import com.imcys.bilibilias.about.GitHubActivity;

import com.imcys.bilibilias.about.SupportActivity;
import com.imcys.bilibilias.home.NewHomeActivity;
import com.kongzue.dialogx.dialogs.InputDialog;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.interfaces.OnInputDialogButtonClickListener;
import com.thl.filechooser.FileChooser;
import com.thl.filechooser.FileInfo;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.regex.Pattern;


public class SettingActivity extends PreferenceFragment {

    private Preference mDLPreference;
    private AppFilePathUtils mAppFilePathUtils;
    private Preference mClearCachePreference;
    private SharedPreferences sharedPreferences;
    private Preference mDLNamePreference;
    private Preference mSourcePreference;
    private Preference mSupportPreference;
    private Preference mCodeSource;
    private int AndroidR_File_ID = 1;
    private int SetDlPath_ID = 2;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);

        //基础配置
        Context mContext = getActivity();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        //绑定控件
        findPreference();

        //设置显示
        mDLPreference.setSummary(sharedPreferences.getString("DownloadPath", getString(R.string.DownloadPath)));
        mDLNamePreference.setSummary(sharedPreferences.getString("DownloadName", "P{P}-{P_TITLE}-{CID}.{VIDEO_TYPE}"));

        //设置点击事件
        mDLPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SetDLPath();
                return true;
            }
        });


        //清理缓存
        mClearCachePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                goClearCache();
                return true;
            }
        });

        //设置命名规则
        mDLNamePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                setDLName();
                return false;
            }
        });

        mSourcePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), GitHubActivity.class);
                startActivity(intent);
                return false;
            }
        });

        mSupportPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), SupportActivity.class);
                startActivity(intent);
                return false;
            }
        });

        //前往GitHub
        mCodeSource.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://github.com/1250422131/bilibilias");//此处填链接
                intent.setData(content_url);
                startActivity(intent);
                return false;
            }
        });

        setCacheSize();

    }


    private void findPreference() {
        mSupportPreference = (Preference) findPreference("setting_set_support");
        mDLPreference = (Preference) findPreference("setting_set_DLPath");
        mClearCachePreference = (Preference) findPreference("setting_set_ClearCache");
        mDLNamePreference = (Preference) findPreference("setting_set_DLName");
        mSourcePreference = (Preference) findPreference("setting_set_source");
        mCodeSource = (Preference) findPreference("setting_source");
    }


    public void SetDLPath() {
        FileChooser fileChooser = new FileChooser(getActivity(), new FileChooser.FileChoosenListener() {
            @Override
            public void onFileChoosen(String filePath) {
                mDLPreference.setSummary(filePath + "/");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("DownloadPath", filePath + "/");
                editor.apply();
            }
        });
        fileChooser.setTitle("选择储存目录");
        fileChooser.setDoneText("确定");
        fileChooser.setThemeColor(R.color.colorAccent);
        fileChooser.setChooseType(FileInfo.FILE_TYPE_FOLDER);
        fileChooser.showFile(false);  //是否显示文件
        fileChooser.open();
    }

    public void goClearCache() {
        mAppFilePathUtils = new AppFilePathUtils(getActivity(), "com.imcys.bilibilias");
        MessageDialog.build()
                .setTitle("清理提示")
                .setMessage("嘻嘻，这个是运行软件过程产生的垃圾文件，不是下载的视频哦，放心清理吧！")
                //设置按钮文本并设置回调
                .setOkButton("清理缓存", new OnDialogButtonClickListener<MessageDialog>() {
                    @Override
                    public boolean onClick(MessageDialog baseDialog, View v) {
                        AppFilePathUtils.deleteDir(mAppFilePathUtils.getCache());
                        Toast.makeText(getActivity(), "清理完成", Toast.LENGTH_SHORT).show();
                        setCacheSize();
                        return false;
                    }
                })
                .setCancelButton("我内存大", null).show();
    }

    private void setCacheSize() {
        mAppFilePathUtils = new AppFilePathUtils(getActivity(), "com.imcys.bilibilias");
        final long total = AppFilePathUtils.getTotalSizeOfFilesInDir(mAppFilePathUtils.getCache());
        double cacheNc = (double) (total / 1048576);
        mClearCachePreference.setSummary(cacheNc + "MB");
    }

    private void setDLName() {
        new InputDialog("命名规则", getString(R.string.Set_DLName_message), "确定", "取消", null)
                .setCancelable(false)
                .setInputHintText(sharedPreferences.getString("DownloadName", "P{P}-{P_TITLE}-{CID}.{VIDEO_TYPE}"))
                .setOkButton(new OnInputDialogButtonClickListener<InputDialog>() {
                    @Override
                    public boolean onClick(InputDialog baseDialog, View v, String inputStr) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        boolean containsResult = !inputStr.contains(".{VIDEO_TYPE}"); //包含则返回 0 相当于异或 1和1 返回 0 0和1 返回 1
                        containsResult = containsResult | Pattern.matches(".*([<>:*?\"]).*", inputStr); //包含则返回1
                        if (containsResult) {
                            Toast.makeText(getActivity(), "有非法字符或者没补全文件格式", Toast.LENGTH_SHORT).show();
                            return true;
                        } else {
                            mDLNamePreference.setSummary(inputStr);
                            editor.putString("DownloadName", inputStr);
                            editor.apply();
                            return false;
                        }
                    }
                })
                .show();
    }


    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onResume(getActivity());
    }
}
