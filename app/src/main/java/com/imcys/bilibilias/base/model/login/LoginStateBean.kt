package com.imcys.bilibilias.base.model.login;

import java.io.Serializable;

/**
 * @author:imcys
 * @create: 2022-10-26 17:30
 * @Description: 登陆状态数据
 */
public class LoginStateBean implements Serializable {

    /**
     * code : 0
     * message : 0
     * ttl : 1
     * data : {"url":"","refresh_token":"","timestamp":0,"code":86038,"message":"二维码已失效"}
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
         * url :
         * refresh_token :
         * timestamp : 0
         * code : 86038
         * message : 二维码已失效
         */

        private String url;
        private String refresh_token;
        private Long timestamp;
        private int code;
        private String message;

        @Override
        public String toString() {
            return "DataBean{" +
                    "url='" + url + '\'' +
                    ", refresh_token='" + refresh_token + '\'' +
                    ", timestamp=" + timestamp +
                    ", code=" + code +
                    ", message='" + message + '\'' +
                    '}';
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

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
    }
}
