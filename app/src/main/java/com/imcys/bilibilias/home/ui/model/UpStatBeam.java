package com.imcys.bilibilias.home.ui.model;

import java.io.Serializable;

/**
 * @author:imcys
 * @create: 2022-12-27 13:12
 * @Description:
 */
public class UpStatBeam implements Serializable {

    /**
     * code : 0
     * message : 0
     * ttl : 1
     * data : {"archive":{"view":2538635},"article":{"view":27607},"likes":79865}
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
         * archive : {"view":2538635}
         * article : {"view":27607}
         * likes : 79865
         */

        private ArchiveBean archive;
        private ArticleBean article;
        private int likes;

        public ArchiveBean getArchive() {
            return archive;
        }

        public void setArchive(ArchiveBean archive) {
            this.archive = archive;
        }

        public ArticleBean getArticle() {
            return article;
        }

        public void setArticle(ArticleBean article) {
            this.article = article;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public static class ArchiveBean implements Serializable {
            /**
             * view : 2538635
             */

            private int view;

            public int getView() {
                return view;
            }

            public void setView(int view) {
                this.view = view;
            }
        }

        public static class ArticleBean implements Serializable {
            /**
             * view : 27607
             */

            private int view;

            public int getView() {
                return view;
            }

            public void setView(int view) {
                this.view = view;
            }
        }
    }
}
