package com.imcys.bilibilias.home.ui.model;

import java.io.Serializable;

/**
 * @author:imcys
 * @create: 2023-01-03 16:03
 * @Description: 检验是否被点赞
 */
public class ArchiveHasLikeBean implements Serializable {

    /**
     * code : 0
     * message : 0
     * ttl : 1
     * data : 1
     */

    private int code;
    private String message;
    private int ttl;
    private int data;

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

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
