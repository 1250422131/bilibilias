package com.imcys.bilibilias.home.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author:imcys
 * @create: 2023-01-20 14:45
 * @Description: 工具页类
 */
public class OldToolItemBean implements Serializable {

    /**
     * code : 0
     * data : [{"tool_code":1,"color":"","title":"缓存视频","img_url":"https://s1.ax1x.com/2022/12/18/zbTmpF.png"},{"tool_code":2,"color":"","title":"缓存视频","img_url":"https://s1.ax1x.com/2022/12/18/zbTmpF.png"}]
     */

    private int code;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * tool_code : 1
         * color :
         * title : 缓存视频
         * img_url : https://s1.ax1x.com/2022/12/18/zbTmpF.png
         */

        private int tool_code;
        private String color;
        private String title;
        private String img_url;

        public int getTool_code() {
            return tool_code;
        }

        public void setTool_code(int tool_code) {
            this.tool_code = tool_code;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }
    }
}

