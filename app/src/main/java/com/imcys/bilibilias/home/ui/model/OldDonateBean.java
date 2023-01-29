package com.imcys.bilibilias.home.ui.model;

import java.io.Serializable;

/**
 * @author:imcys
 * @create: 2023-01-02 17:18
 * @Description: 旧的捐款数据
 */
public class OldDonateBean implements Serializable {

    /**
     * code : 0
     * Alipay : https://view.misakamoe.com/donate/Alipay.jpg
     * WeChat : https://view.misakamoe.com/donate/WeChat.jpg
     * Surplus : 32.00
     * Total : 50
     */

    private int code;
    private String Alipay;
    private String WeChat;
    private String Surplus;
    private String Total;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getAlipay() {
        return Alipay;
    }

    public void setAlipay(String Alipay) {
        this.Alipay = Alipay;
    }

    public String getWeChat() {
        return WeChat;
    }

    public void setWeChat(String WeChat) {
        this.WeChat = WeChat;
    }

    public String getSurplus() {
        return Surplus;
    }

    public void setSurplus(String Surplus) {
        this.Surplus = Surplus;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String Total) {
        this.Total = Total;
    }
}
