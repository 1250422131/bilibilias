package com.imcys.bilibilias.home.ui.model;

import java.io.Serializable;

/**
 * @author:imcys
 * @create: 2022-11-30 18:47
 * @Description: 投币结果类
 */
public class VideoCoinAddBean implements Serializable {


    /**
     * code : 0
     * message : 0
     * ttl : 1
     * data : {"like":true}
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
         * like : true
         */

        private boolean like;

        public boolean isLike() {
            return like;
        }

        public void setLike(boolean like) {
            this.like = like;
        }
    }
}
