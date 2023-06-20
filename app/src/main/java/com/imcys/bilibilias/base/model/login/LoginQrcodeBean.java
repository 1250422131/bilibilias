package com.imcys.bilibilias.base.model.login;

import java.io.Serializable;

/**
 * @author:imcys
 * @create: 2022-10-26 17:25
 * @Description: 登陆二维码数据
 */
public class LoginQrcodeBean implements Serializable {

    /**
     * code : 0
     * message : 0
     * ttl : 1
     * data : {"url":"https://passport.bilibili.com/h5-app/passport/login/scan?navhide=1&qrcode_key=ede6c0dde9d2a25eee16ff596b4385e3&from=","qrcode_key":"ede6c0dde9d2a25eee16ff596b4385e3"}
     */

    private int code;
    private String message;
    private int ttl;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * url : https://passport.bilibili.com/h5-app/passport/login/scan?navhide=1&qrcode_key=ede6c0dde9d2a25eee16ff596b4385e3&from=
         * qrcode_key : ede6c0dde9d2a25eee16ff596b4385e3
         */

        private String url;
        private String qrcode_key;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getQrcode_key() {
            return qrcode_key;
        }

        public void setQrcode_key(String qrcode_key) {
            this.qrcode_key = qrcode_key;
        }
    }
}

