package com.imcys.bilibilias.common.base.model.user;

import java.io.Serializable;

/**
 * imcys
 *
 * @create: 2022-10-30 08:12
 * @Description: 我的个人信息类
 */
public class MyUserData implements Serializable {

    /**
     * code : 0
     * message : 0
     * ttl : 1
     * data : {"mid":293793435,"uname":"社会易姐QwQ","userid":"bili_84675323391","sign":"高中技术宅一枚，爱好MC&电子&8-bit音乐&数码&编程，资深猿厨，粉丝群：1136462265","birthday":"2002-03-05","sex":"男","nick_free":false,"rank":"正式会员"}
     */

    private int code;
    private String message;
    private int ttl;
    private DataBean data;

    @Override
    public String toString() {
        return "MyUserData{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", ttl=" + ttl +
                ", data=" + data +
                '}';
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
        private long mid;
        private String uname;
        private String userid;
        private String sign;
        private String birthday;
        private String sex;
        private boolean nick_free;
        private String rank;

        public long getMid() {
            return mid;
        }

        public void setMid(long mid) {
            this.mid = mid;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public boolean isNick_free() {
            return nick_free;
        }

        public void setNick_free(boolean nick_free) {
            this.nick_free = nick_free;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }
    }
}
