package com.imcys.bilibilias.home.ui.model;

import java.io.Serializable;

/**
 * @author:imcys
 * @create: 2022-11-30 15:46
 * @Description: 收藏结果数据类
 */
public class CollectionResultBean implements Serializable {

    /**
     * code : 0
     * data : {"prompt":false}
     * message : success
     */

    private int code;
    private DataBean data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean implements Serializable {
        /**
         * prompt : false
         */

        private boolean prompt;

        public boolean isPrompt() {
            return prompt;
        }

        public void setPrompt(boolean prompt) {
            this.prompt = prompt;
        }
    }
}
