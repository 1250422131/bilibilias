package com.liulishuo.okdownload.core.logger;

public class EmptyLogger implements Logger {
    @Override
    public void e(String tag, String msg, Exception e) {
    }

    @Override
    public void w(String tag, String msg) {
    }

    @Override
    public void d(String tag, String msg) {
    }

    @Override
    public void i(String tag, String msg) {
    }
}