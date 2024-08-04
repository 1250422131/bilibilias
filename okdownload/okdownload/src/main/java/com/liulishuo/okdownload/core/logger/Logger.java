package com.liulishuo.okdownload.core.logger;

public interface Logger {
    void e(String tag, String msg, Exception e);

    void w(String tag, String msg);

    void d(String tag, String msg);

    void i(String tag, String msg);
}