package com.imcys.bilibilias.home.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author:imcys
 * @create: 2023-01-01 18:05
 * @Description: 旧版首页轮播图数据
 */
public class OldHomeBannerDataBean implements Serializable {

    /**
     * code : 0
     * imgUrlList : ["https://i.328888.xyz/img/2022/11/30/ikl2C.jpeg","http://message.biliimg.com/bfs/im/500ac4d77ef89bcfc3ae503ec3a8bb72a1a483fa.png","https://i.loli.net/2021/04/03/mTuUekRxQ5HrFIB.png","https://i.loli.net/2021/02/23/ErYSKClUhatXxVR.png","https://s3.ax1x.com/2021/01/16/sBYWkD.png","https://i.loli.net/2021/06/13/ShOYsRWHAeFD5iz.png"]
     * textList : ["【致哀】江泽民同志逝世","Q1季度更全面的报表","黑科技","我们有新的计划啦!!","严禁视频二次发布，珍惜UP主的付出","这些功能你知道吗？"]
     * typeList : ["goUrl","goUrl","goUrl","goUrl","goUrl","goUrl"]
     * dataList : ["https://mbd.baidu.com/newspage/data/landingsuper?pageType=1&context=%7B%22nid%22%3A%22news_9281076262358294005%22,%22sourceFrom%22%3A%22bjh%22%7D","https://imcys.com/2022/05/01/bilibiliasq1%e5%ad%a3%e5%ba%a6%e6%8a%a5%e5%91%8a-2022.html","https://support.qq.com/products/337496/faqs/100957","https://docs.qq.com/doc/DVVdQa1J5aGxJcm5Y","https://api.misakamoe.com/app/statistics.html","https://support.qq.com/products/337496/faqs/98923"]
     * successToast : ["",null,null,null,null,null]
     * failToast : ["",null,null,null,null,null]
     * postData : ["",null,null,null,null,null]
     * token : ["",null,null,null,null,null]
     * time : 7000
     */

    private int code;
    private int time;
    private List<String> imgUrlList;
    private List<String> textList;
    private List<String> typeList;
    private List<String> dataList;
    private List<String> successToast;
    private List<String> failToast;
    private List<String> postData;
    private List<Integer> token;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<String> getImgUrlList() {
        return imgUrlList;
    }

    public void setImgUrlList(List<String> imgUrlList) {
        this.imgUrlList = imgUrlList;
    }

    public List<String> getTextList() {
        return textList;
    }

    public void setTextList(List<String> textList) {
        this.textList = textList;
    }

    public List<String> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<String> typeList) {
        this.typeList = typeList;
    }

    public List<String> getDataList() {
        return dataList;
    }

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
    }

    public List<String> getSuccessToast() {
        return successToast;
    }

    public void setSuccessToast(List<String> successToast) {
        this.successToast = successToast;
    }

    public List<String> getFailToast() {
        return failToast;
    }

    public void setFailToast(List<String> failToast) {
        this.failToast = failToast;
    }

    public List<String> getPostData() {
        return postData;
    }

    public void setPostData(List<String> postData) {
        this.postData = postData;
    }

    public List<Integer> getToken() {
        return token;
    }

    public void setToken(List<Integer> token) {
        this.token = token;
    }
}
