package com.imcys.bilibilias.home.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author:imcys
 * @create: 2022-11-16 20:19
 * @Description: 用户创建收藏夹数据
 */


public class UserCreateCollectionBean implements Serializable {

    /**
     * code : 0
     * message : 0
     * ttl : 1
     * data : {"count":6,"list":[{"id":196026807,"fid":1960268,"mid":351201307,"attr":0,"title":"默认收藏夹","fav_state":0,"media_count":452},{"id":1429039607,"fid":14290396,"mid":351201307,"attr":2,"title":"物理学习","fav_state":0,"media_count":1},{"id":1426414307,"fid":14264143,"mid":351201307,"attr":2,"title":"设计学习","fav_state":0,"media_count":1},{"id":1292535107,"fid":12925351,"mid":351201307,"attr":54,"title":"致我童年","fav_state":0,"media_count":8},{"id":1274129407,"fid":12741294,"mid":351201307,"attr":2,"title":"生物学习","fav_state":0,"media_count":3},{"id":1224727107,"fid":12247271,"mid":351201307,"attr":2,"title":"思想政治","fav_state":0,"media_count":11}],"season":null}
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
         * count : 6
         * list : [{"id":196026807,"fid":1960268,"mid":351201307,"attr":0,"title":"默认收藏夹","fav_state":0,"media_count":452},{"id":1429039607,"fid":14290396,"mid":351201307,"attr":2,"title":"物理学习","fav_state":0,"media_count":1},{"id":1426414307,"fid":14264143,"mid":351201307,"attr":2,"title":"设计学习","fav_state":0,"media_count":1},{"id":1292535107,"fid":12925351,"mid":351201307,"attr":54,"title":"致我童年","fav_state":0,"media_count":8},{"id":1274129407,"fid":12741294,"mid":351201307,"attr":2,"title":"生物学习","fav_state":0,"media_count":3},{"id":1224727107,"fid":12247271,"mid":351201307,"attr":2,"title":"思想政治","fav_state":0,"media_count":11}]
         * season : null
         */

        private int count;
        private Object season;
        private List<ListBean> list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public Object getSeason() {
            return season;
        }

        public void setSeason(Object season) {
            this.season = season;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean implements Serializable {
            /**
             * id : 196026807
             * fid : 1960268
             * mid : 351201307
             * attr : 0
             * title : 默认收藏夹
             * fav_state : 0
             * media_count : 452
             */

            private long id;
            private long fid;
            private long mid;
            private int attr;
            private String title;
            private int fav_state;
            private int media_count;
            private int selected = 0;

            public int getSelected() {
                return selected;
            }

            public void setSelected(int selected) {
                this.selected = selected;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public long getFid() {
                return fid;
            }

            public void setFid(long fid) {
                this.fid = fid;
            }

            public long getMid() {
                return mid;
            }

            public void setMid(long mid) {
                this.mid = mid;
            }

            public int getAttr() {
                return attr;
            }

            public void setAttr(int attr) {
                this.attr = attr;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getFav_state() {
                return fav_state;
            }

            public void setFav_state(int fav_state) {
                this.fav_state = fav_state;
            }

            public int getMedia_count() {
                return media_count;
            }

            public void setMedia_count(int media_count) {
                this.media_count = media_count;
            }
        }
    }
}
