package com.imcys.bilibilias.home.ui.model;

import java.io.Serializable;

/**
 * @author:imcys
 * @create: 2023-01-01 17:17
 * @Description: 旧的后端更新类
 */
public class OldUpdateDataBean implements Serializable {


    /**
     * code : 0
     * version : 2.2
     * gxnotice : 更新有问题联系
     * <p>
     * QQ1250422131
     * <p>
     * 1.1.0更新
     * <p>
     * 为了对抗盗版，我们不得不进行更新。
     * <p>
     * 1.支持批量下载
     * <p>
     * 2.全局UI美化
     * <p>
     * 3.支持UP主视频冻结【保护UP视频】
     * <p>
     * 4.支持桌面小部件【粉丝显示器】
     * notice : bilibilias全程采用B站自身服务器处理下载，严禁使用该程序做为非法用途，一切后果由使用者自行承担。
     * <p>
     * 特别注意！！！B站UP们视频创作都非常不易，大多数视频都写了禁止二次转载，所以我们严禁用户使用下载的视频进行二次发布，或者转载时以任何方式抹除，不标注，不在显眼处标注来源B站链接，UP等视频相关信息，当然你可以争取UP主同意后使用，违反者由使用者承担一切后果，这包括法律责任，使用本程序代表同意且接受本公告所写责任。
     * <p>
     * 我们这里的画质是根据你是否有会员来判断的，我们知道，有些视频较为高的画质需要会员缓存，那么在bilibilias，你必须登录会员账号才能下载，否则无法获取下载地址。
     * 严禁二改本程序，本程序免费使用，严禁用于商业用途！！！！
     * 软件仅供学习交流，请在24小时内删除。
     * 使用本程序代表同意上述内容
     * url : https://api.misakamoe.com/app/
     * APKMD5 : 0
     * APKToKen : 0
     * APKToKenCR : 0
     * ID : 2
     */

    private int code;
    private String version;
    private String gxnotice;
    private String notice;
    private String url;
    private String APKMD5;
    private String APKToKen;
    private String APKToKenCR;
    private String ID;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGxnotice() {
        return gxnotice;
    }

    public void setGxnotice(String gxnotice) {
        this.gxnotice = gxnotice;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAPKMD5() {
        return APKMD5;
    }

    public void setAPKMD5(String APKMD5) {
        this.APKMD5 = APKMD5;
    }

    public String getAPKToKen() {
        return APKToKen;
    }

    public void setAPKToKen(String APKToKen) {
        this.APKToKen = APKToKen;
    }

    public String getAPKToKenCR() {
        return APKToKenCR;
    }

    public void setAPKToKenCR(String APKToKenCR) {
        this.APKToKenCR = APKToKenCR;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
