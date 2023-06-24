package com.imcys.bilibilias.home.ui.model;

import java.io.Serializable;

/**
 * @author:imcys
 * @create: 2023-01-03 16:23
 * @Description: 收藏检验
 */
public class ArchiveFavouredBean implements Serializable {

    /**
     * code : 0
     * message : 0
     * ttl : 1
     * data : {"count":1,"favoured":true}
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
         * count : 1
         * favoured : true
         */

        private int count;
        private boolean favoured;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public boolean isFavoured() {
            return favoured;
        }

        public void setFavoured(boolean favoured) {
            this.favoured = favoured;
        }
    }
}
