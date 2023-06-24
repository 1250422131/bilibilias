package com.imcys.bilibilias.home.ui.model;

import java.io.Serializable;

/**
 * @author:imcys
 * @create: 2023-01-03 16:18
 * @Description: 投币情况嘞
 */
public class ArchiveCoinsBean implements Serializable {

    /**
     * code : 0
     * message : 0
     * ttl : 1
     * data : {"multiply":2}
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
         * multiply : 2
         */

        private int multiply;

        public int getMultiply() {
            return multiply;
        }

        public void setMultiply(int multiply) {
            this.multiply = multiply;
        }
    }
}
