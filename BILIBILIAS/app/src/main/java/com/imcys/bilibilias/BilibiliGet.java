package com.imcys.bilibilias;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.baidu.mobstat.StatService;
import com.imcys.bilibilias.home.NewHomeActivity;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.style.MIUIStyle;

import org.xutils.x;

public class BilibiliGet extends Application {

    private static BilibiliGet bilibiliGet;


    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(false); //是否输出debug日志，开启debug会影响性能。

        //DialogX
        DialogX.init(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String DialogXStyle = sharedPreferences.getString("DialogXStyle", "1");
        if (!DialogXStyle.equals("1")) {
            DialogX.globalStyle = new MIUIStyle();
        }
        DialogX.globalTheme = DialogX.THEME.LIGHT;


        //百度统计
        StatService.setAuthorizedState(getApplicationContext(), true);
        StatService.autoTrace(this);

    }


}