package com.imcys.bilibilias;

import android.app.Application;

import org.xutils.x;

public class BilibiliGet extends Application {
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true); //是否输出debug日志，开启debug会影响性能。
    }
}