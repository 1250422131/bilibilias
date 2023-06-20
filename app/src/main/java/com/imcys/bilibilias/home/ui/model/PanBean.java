package com.imcys.bilibilias.home.ui.model;


import java.io.Serializable;
import java.util.List;

public class PanBean implements Serializable {

    /**
     * res : {"code":0,"message":"ok","data":{"UserNickName":"tallsister","ShareName":"WeChat 8.0.30(2240).apk","HasPwd":false,"Expiration":"2099-01-01T00:00:00+08:00","CreateAt":"2022-11-21T23:48:36+08:00","Expired":false,"ShareKey":"fSQ0Vv-4bm13","HeadImage":"https://download.123pan.cn/123-pics/head-pic/1812517999.jpg?v=1&amp;t=4820118524&amp;s=6ab36e49cd220d786f98c4d0ebafd795"}}
     * reslist : {"code":0,"message":"ok","data":{"Next":"-1","Len":1,"IsFirst":true,"Expired":false,"InfoList":[{"FileId":1835306,"FileName":"WeChat 8.0.30(2240).apk","Type":0,"Size":488103719,"ContentType":"0","S3KeyFlag":"1813124589-0","CreateAt":"2022-11-21T23:46:35+08:00","UpdateAt":"2022-11-22T17:00:40+08:00","Etag":"79fca5071213d611c1910491a2a66772","DownloadUrl":"","Status":2,"ParentFileId":1766237,"Category":0,"PunishFlag":0,"checked":false}]}}
     * publicPath : https://www.123pan.com/a/api/
     */
    private ResBean res;
    private ReslistBean reslist;
    private String publicPath;


    @Override
    public String toString() {
        return "PanBean{" +
                "res=" + res +
                ", reslist=" + reslist +
                ", publicPath='" + publicPath + '\'' +
                '}';
    }

    public ResBean getRes() {
        return res;
    }

    public void setRes(ResBean res) {
        this.res = res;
    }

    public ReslistBean getReslist() {
        return reslist;
    }

    public void setReslist(ReslistBean reslist) {
        this.reslist = reslist;
    }

    public String getPublicPath() {
        return publicPath;
    }

    public void setPublicPath(String publicPath) {
        this.publicPath = publicPath;
    }

    public static class ResBean implements Serializable {
        /**
         * code : 0
         * message : ok
         * data : {"UserNickName":"tallsister","ShareName":"WeChat 8.0.30(2240).apk","HasPwd":false,"Expiration":"2099-01-01T00:00:00+08:00","CreateAt":"2022-11-21T23:48:36+08:00","Expired":false,"ShareKey":"fSQ0Vv-4bm13","HeadImage":"https://download.123pan.cn/123-pics/head-pic/1812517999.jpg?v=1&amp;t=4820118524&amp;s=6ab36e49cd220d786f98c4d0ebafd795"}
         */

        private int code;
        private String message;
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

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean implements Serializable {
            /**
             * UserNickName : tallsister
             * ShareName : WeChat 8.0.30(2240).apk
             * HasPwd : false
             * Expiration : 2099-01-01T00:00:00+08:00
             * CreateAt : 2022-11-21T23:48:36+08:00
             * Expired : false
             * ShareKey : fSQ0Vv-4bm13
             * HeadImage : https://download.123pan.cn/123-pics/head-pic/1812517999.jpg?v=1&amp;t=4820118524&amp;s=6ab36e49cd220d786f98c4d0ebafd795
             */

            private String UserNickName;
            private String ShareName;
            private boolean HasPwd;
            private String Expiration;
            private String CreateAt;
            private boolean Expired;
            private String ShareKey;
            private String HeadImage;

            public String getUserNickName() {
                return UserNickName;
            }

            public void setUserNickName(String UserNickName) {
                this.UserNickName = UserNickName;
            }

            public String getShareName() {
                return ShareName;
            }

            public void setShareName(String ShareName) {
                this.ShareName = ShareName;
            }

            public boolean isHasPwd() {
                return HasPwd;
            }

            public void setHasPwd(boolean HasPwd) {
                this.HasPwd = HasPwd;
            }

            public String getExpiration() {
                return Expiration;
            }

            public void setExpiration(String Expiration) {
                this.Expiration = Expiration;
            }

            public String getCreateAt() {
                return CreateAt;
            }

            public void setCreateAt(String CreateAt) {
                this.CreateAt = CreateAt;
            }

            public boolean isExpired() {
                return Expired;
            }

            public void setExpired(boolean Expired) {
                this.Expired = Expired;
            }

            public String getShareKey() {
                return ShareKey;
            }

            public void setShareKey(String ShareKey) {
                this.ShareKey = ShareKey;
            }

            public String getHeadImage() {
                return HeadImage;
            }

            public void setHeadImage(String HeadImage) {
                this.HeadImage = HeadImage;
            }
        }
    }

    public static class ReslistBean implements Serializable {
        /**
         * code : 0
         * message : ok
         * data : {"Next":"-1","Len":1,"IsFirst":true,"Expired":false,"InfoList":[{"FileId":1835306,"FileName":"WeChat 8.0.30(2240).apk","Type":0,"Size":488103719,"ContentType":"0","S3KeyFlag":"1813124589-0","CreateAt":"2022-11-21T23:46:35+08:00","UpdateAt":"2022-11-22T17:00:40+08:00","Etag":"79fca5071213d611c1910491a2a66772","DownloadUrl":"","Status":2,"ParentFileId":1766237,"Category":0,"PunishFlag":0,"checked":false}]}
         */

        private int code;
        private String message;
        private DataBeanX data;

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

        public DataBeanX getData() {
            return data;
        }

        public void setData(DataBeanX data) {
            this.data = data;
        }

        public static class DataBeanX implements Serializable {
            /**
             * Next : -1
             * Len : 1
             * IsFirst : true
             * Expired : false
             * InfoList : [{"FileId":1835306,"FileName":"WeChat 8.0.30(2240).apk","Type":0,"Size":488103719,"ContentType":"0","S3KeyFlag":"1813124589-0","CreateAt":"2022-11-21T23:46:35+08:00","UpdateAt":"2022-11-22T17:00:40+08:00","Etag":"79fca5071213d611c1910491a2a66772","DownloadUrl":"","Status":2,"ParentFileId":1766237,"Category":0,"PunishFlag":0,"checked":false}]
             */


            private String Next;
            private int Len;
            private boolean IsFirst;
            private boolean Expired;
            private List<InfoListBean> InfoList;

            @Override
            public String toString() {
                return "DataBeanX{" +
                        "Next='" + Next + '\'' +
                        ", Len=" + Len +
                        ", IsFirst=" + IsFirst +
                        ", Expired=" + Expired +
                        ", InfoList=" + InfoList +
                        '}';
            }

            public String getNext() {
                return Next;
            }

            public void setNext(String Next) {
                this.Next = Next;
            }

            public int getLen() {
                return Len;
            }

            public void setLen(int Len) {
                this.Len = Len;
            }

            public boolean isIsFirst() {
                return IsFirst;
            }

            public void setIsFirst(boolean IsFirst) {
                this.IsFirst = IsFirst;
            }

            public boolean isExpired() {
                return Expired;
            }

            public void setExpired(boolean Expired) {
                this.Expired = Expired;
            }

            public List<InfoListBean> getInfoList() {
                return InfoList;
            }

            public void setInfoList(List<InfoListBean> InfoList) {
                this.InfoList = InfoList;
            }

            public static class InfoListBean implements Serializable {
                /**
                 * FileId : 1835306
                 * FileName : WeChat 8.0.30(2240).apk
                 * Type : 0
                 * Size : 488103719
                 * ContentType : 0
                 * S3KeyFlag : 1813124589-0
                 * CreateAt : 2022-11-21T23:46:35+08:00
                 * UpdateAt : 2022-11-22T17:00:40+08:00
                 * Etag : 79fca5071213d611c1910491a2a66772
                 * DownloadUrl :
                 * Status : 2
                 * ParentFileId : 1766237
                 * Category : 0
                 * PunishFlag : 0
                 * checked : false
                 */

                private int FileId;
                private String FileName;
                private int Type;
                private int Size;
                private String ContentType;
                private String S3KeyFlag;
                private String CreateAt;
                private String UpdateAt;
                private String Etag;
                private String DownloadUrl;
                private int Status;
                private int ParentFileId;
                private int Category;
                private int PunishFlag;
                private boolean checked;

                public int getFileId() {
                    return FileId;
                }

                public void setFileId(int FileId) {
                    this.FileId = FileId;
                }

                public String getFileName() {
                    return FileName;
                }

                public void setFileName(String FileName) {
                    this.FileName = FileName;
                }

                public int getType() {
                    return Type;
                }

                public void setType(int Type) {
                    this.Type = Type;
                }

                public int getSize() {
                    return Size;
                }

                public void setSize(int Size) {
                    this.Size = Size;
                }

                public String getContentType() {
                    return ContentType;
                }

                public void setContentType(String ContentType) {
                    this.ContentType = ContentType;
                }

                public String getS3KeyFlag() {
                    return S3KeyFlag;
                }

                public void setS3KeyFlag(String S3KeyFlag) {
                    this.S3KeyFlag = S3KeyFlag;
                }

                public String getCreateAt() {
                    return CreateAt;
                }

                public void setCreateAt(String CreateAt) {
                    this.CreateAt = CreateAt;
                }

                public String getUpdateAt() {
                    return UpdateAt;
                }

                public void setUpdateAt(String UpdateAt) {
                    this.UpdateAt = UpdateAt;
                }

                public String getEtag() {
                    return Etag;
                }

                public void setEtag(String Etag) {
                    this.Etag = Etag;
                }

                public String getDownloadUrl() {
                    return DownloadUrl;
                }

                public void setDownloadUrl(String DownloadUrl) {
                    this.DownloadUrl = DownloadUrl;
                }

                public int getStatus() {
                    return Status;
                }

                public void setStatus(int Status) {
                    this.Status = Status;
                }

                public int getParentFileId() {
                    return ParentFileId;
                }

                public void setParentFileId(int ParentFileId) {
                    this.ParentFileId = ParentFileId;
                }

                public int getCategory() {
                    return Category;
                }

                public void setCategory(int Category) {
                    this.Category = Category;
                }

                public int getPunishFlag() {
                    return PunishFlag;
                }

                public void setPunishFlag(int PunishFlag) {
                    this.PunishFlag = PunishFlag;
                }

                public boolean isChecked() {
                    return checked;
                }

                public void setChecked(boolean checked) {
                    this.checked = checked;
                }
            }
        }
    }
}
