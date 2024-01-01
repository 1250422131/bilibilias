package com.imcys.bilibilias.home.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author:imcys
 * @create: 2022-11-18 17:01
 * @Description: 视频下载信息数据类
 */
public class VideoPlayBean implements Serializable {


    /**
     * code : 0
     * message : 0
     * ttl : 1
     * data : {"from":"local","result":"suee","message":"","quality":16,"format":"mp4","timelength":233643,"accept_format":"mp4,mp4","accept_description":["高清 1080P","流畅 360P"],"accept_quality":[80,16],"video_codecid":7,"seek_param":"start","seek_type":"second","durl":[{"order":1,"length":233643,"size":12448260,"ahead":"","vhead":"","url":"https://xy123x246x198x205xy.mcdn.bilivideo.cn:4483/upgcxcode/72/03/429140372/429140372-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1668752789&gen=playurlv2&os=mcdn&oi=1781509104&trid=0000097663fae882444ea5fdb03b1425fb30u&mid=351201307&platform=pc&upsig=98cd9cd8ed75a08f6219513dfbcbef37&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=1002547&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=53426&logo=A0000001","backup_url":["https://upos-sz-estgoss.bilivideo.com/upgcxcode/72/03/429140372/429140372-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1668752789&gen=playurlv2&os=upos&oi=1781509104&trid=097663fae882444ea5fdb03b1425fb30u&mid=351201307&platform=pc&upsig=b64e6ad943b5e583885e5fc708216048&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=53426&logo=40000000","https://upos-sz-mirrorali.bilivideo.com/upgcxcode/72/03/429140372/429140372-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1668752789&gen=playurlv2&os=alibv&oi=1781509104&trid=097663fae882444ea5fdb03b1425fb30u&mid=351201307&platform=pc&upsig=c46ad331f318af00c9fb221e70920e7b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=53426&logo=40000000"]}],"support_formats":[{"quality":80,"format":"mp4","new_description":"1080P 高清","display_desc":"1080P","superscript":"","codecs":null},{"quality":16,"format":"mp4","new_description":"360P 流畅","display_desc":"360P","superscript":"","codecs":null}],"high_format":null,"last_play_time":0,"last_play_cid":0}
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
         * from : local
         * result : suee
         * message :
         * quality : 16
         * format : mp4
         * timelength : 233643
         * accept_format : mp4,mp4
         * accept_description : ["高清 1080P","流畅 360P"]
         * accept_quality : [80,16]
         * video_codecid : 7
         * seek_param : start
         * seek_type : second
         * durl : [{"order":1,"length":233643,"size":12448260,"ahead":"","vhead":"","url":"https://xy123x246x198x205xy.mcdn.bilivideo.cn:4483/upgcxcode/72/03/429140372/429140372-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1668752789&gen=playurlv2&os=mcdn&oi=1781509104&trid=0000097663fae882444ea5fdb03b1425fb30u&mid=351201307&platform=pc&upsig=98cd9cd8ed75a08f6219513dfbcbef37&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=1002547&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=53426&logo=A0000001","backup_url":["https://upos-sz-estgoss.bilivideo.com/upgcxcode/72/03/429140372/429140372-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1668752789&gen=playurlv2&os=upos&oi=1781509104&trid=097663fae882444ea5fdb03b1425fb30u&mid=351201307&platform=pc&upsig=b64e6ad943b5e583885e5fc708216048&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=53426&logo=40000000","https://upos-sz-mirrorali.bilivideo.com/upgcxcode/72/03/429140372/429140372-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1668752789&gen=playurlv2&os=alibv&oi=1781509104&trid=097663fae882444ea5fdb03b1425fb30u&mid=351201307&platform=pc&upsig=c46ad331f318af00c9fb221e70920e7b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=53426&logo=40000000"]}]
         * support_formats : [{"quality":80,"format":"mp4","new_description":"1080P 高清","display_desc":"1080P","superscript":"","codecs":null},{"quality":16,"format":"mp4","new_description":"360P 流畅","display_desc":"360P","superscript":"","codecs":null}]
         * high_format : null
         * last_play_time : 0
         * last_play_cid : 0
         */

        private String from;
        private String result;
        private String message;
        private int quality;
        private String format;
        private int timelength;
        private String accept_format;
        private int video_codecid;
        private String seek_param;
        private String seek_type;
        private Object high_format;
        private int last_play_time;
        private int last_play_cid;
        private List<String> accept_description;
        private List<Integer> accept_quality;
        private List<DurlBean> durl;
        private List<SupportFormatsBean> support_formats;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getQuality() {
            return quality;
        }

        public void setQuality(int quality) {
            this.quality = quality;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public int getTimelength() {
            return timelength;
        }

        public void setTimelength(int timelength) {
            this.timelength = timelength;
        }

        public String getAccept_format() {
            return accept_format;
        }

        public void setAccept_format(String accept_format) {
            this.accept_format = accept_format;
        }

        public int getVideo_codecid() {
            return video_codecid;
        }

        public void setVideo_codecid(int video_codecid) {
            this.video_codecid = video_codecid;
        }

        public String getSeek_param() {
            return seek_param;
        }

        public void setSeek_param(String seek_param) {
            this.seek_param = seek_param;
        }

        public String getSeek_type() {
            return seek_type;
        }

        public void setSeek_type(String seek_type) {
            this.seek_type = seek_type;
        }

        public Object getHigh_format() {
            return high_format;
        }

        public void setHigh_format(Object high_format) {
            this.high_format = high_format;
        }

        public int getLast_play_time() {
            return last_play_time;
        }

        public void setLast_play_time(int last_play_time) {
            this.last_play_time = last_play_time;
        }

        public int getLast_play_cid() {
            return last_play_cid;
        }

        public void setLast_play_cid(int last_play_cid) {
            this.last_play_cid = last_play_cid;
        }

        public List<String> getAccept_description() {
            return accept_description;
        }

        public void setAccept_description(List<String> accept_description) {
            this.accept_description = accept_description;
        }

        public List<Integer> getAccept_quality() {
            return accept_quality;
        }

        public void setAccept_quality(List<Integer> accept_quality) {
            this.accept_quality = accept_quality;
        }

        public List<DurlBean> getDurl() {
            return durl;
        }

        public void setDurl(List<DurlBean> durl) {
            this.durl = durl;
        }

        public List<SupportFormatsBean> getSupport_formats() {
            return support_formats;
        }

        public void setSupport_formats(List<SupportFormatsBean> support_formats) {
            this.support_formats = support_formats;
        }

        public static class DurlBean implements Serializable {
            /**
             * order : 1
             * length : 233643
             * size : 12448260
             * ahead :
             * vhead :
             * url : https://xy123x246x198x205xy.mcdn.bilivideo.cn:4483/upgcxcode/72/03/429140372/429140372-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1668752789&gen=playurlv2&os=mcdn&oi=1781509104&trid=0000097663fae882444ea5fdb03b1425fb30u&mid=351201307&platform=pc&upsig=98cd9cd8ed75a08f6219513dfbcbef37&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=1002547&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=53426&logo=A0000001
             * backup_url : ["https://upos-sz-estgoss.bilivideo.com/upgcxcode/72/03/429140372/429140372-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1668752789&gen=playurlv2&os=upos&oi=1781509104&trid=097663fae882444ea5fdb03b1425fb30u&mid=351201307&platform=pc&upsig=b64e6ad943b5e583885e5fc708216048&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=53426&logo=40000000","https://upos-sz-mirrorali.bilivideo.com/upgcxcode/72/03/429140372/429140372-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1668752789&gen=playurlv2&os=alibv&oi=1781509104&trid=097663fae882444ea5fdb03b1425fb30u&mid=351201307&platform=pc&upsig=c46ad331f318af00c9fb221e70920e7b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=53426&logo=40000000"]
             */

            private int order;
            private int length;
            private Long size;
            private String ahead;
            private String vhead;
            private String url;
            private List<String> backup_url;

            public int getOrder() {
                return order;
            }

            public void setOrder(int order) {
                this.order = order;
            }

            public int getLength() {
                return length;
            }

            public void setLength(int length) {
                this.length = length;
            }

            public Long getSize() {
                return size;
            }

            public void setSize(Long size) {
                this.size = size;
            }

            public String getAhead() {
                return ahead;
            }

            public void setAhead(String ahead) {
                this.ahead = ahead;
            }

            public String getVhead() {
                return vhead;
            }

            public void setVhead(String vhead) {
                this.vhead = vhead;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public List<String> getBackup_url() {
                return backup_url;
            }

            public void setBackup_url(List<String> backup_url) {
                this.backup_url = backup_url;
            }
        }

        public static class SupportFormatsBean implements Serializable {
            /**
             * quality : 80
             * format : mp4
             * new_description : 1080P 高清
             * display_desc : 1080P
             * superscript :
             * codecs : null
             */

            private int quality;
            private String format;
            private String new_description;
            private String display_desc;
            private String superscript;
            private Object codecs;

            public int getQuality() {
                return quality;
            }

            public void setQuality(int quality) {
                this.quality = quality;
            }

            public String getFormat() {
                return format;
            }

            public void setFormat(String format) {
                this.format = format;
            }

            public String getNew_description() {
                return new_description;
            }

            public void setNew_description(String new_description) {
                this.new_description = new_description;
            }

            public String getDisplay_desc() {
                return display_desc;
            }

            public void setDisplay_desc(String display_desc) {
                this.display_desc = display_desc;
            }

            public String getSuperscript() {
                return superscript;
            }

            public void setSuperscript(String superscript) {
                this.superscript = superscript;
            }

            public Object getCodecs() {
                return codecs;
            }

            public void setCodecs(Object codecs) {
                this.codecs = codecs;
            }
        }
    }
}
