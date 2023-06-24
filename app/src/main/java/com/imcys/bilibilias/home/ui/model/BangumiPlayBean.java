package com.imcys.bilibilias.home.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author:imcys
 * @create: 2022-12-17 17:40
 * @Description: 番剧播放类
 */
public class BangumiPlayBean implements Serializable {

    /**
     * code : 0
     * message : success
     * result : {"accept_format":"mp4720,mp4","code":0,"seek_param":"start","is_preview":0,"fnval":0,"video_project":true,"fnver":0,"type":"MP4","bp":0,"result":"suee","seek_type":"second","from":"local","video_codecid":7,"record_info":{"record_icon":"","record":""},"durl":[{"size":10413610,"ahead":"","length":180203,"vhead":"","backup_url":["https://upos-sz-mirrorali.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671275626&gen=playurlv2&os=alibv&oi=1935005709&trid=1f36b0fd30834028bb741ce567e5a5c3p&mid=0&platform=pc&upsig=bb5977ad409d5a92693fe6e3e14c43b1&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=1&bw=57853&logo=40000000","https://upos-sz-mirroralib.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671275626&gen=playurlv2&os=alibbv&oi=1935005709&trid=1f36b0fd30834028bb741ce567e5a5c3p&mid=0&platform=pc&upsig=705db58f0fa5755e11e8cbbcd0d7de5c&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=1&bw=57853&logo=40000000"],"url":"https://upos-sz-estgoss.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671275626&gen=playurlv2&os=upos&oi=1935005709&trid=1f36b0fd30834028bb741ce567e5a5c3p&mid=0&platform=pc&upsig=27779ca7cba1c05977ef4db7d6dee1fc&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=1&bw=57853&logo=80000000","order":1,"md5":""}],"is_drm":false,"no_rexcode":0,"format":"mp4","support_formats":[{"display_desc":"720P","superscript":"","need_login":true,"codecs":[],"format":"mp4720","description":"高清 720P","quality":64,"new_description":"720P 高清"},{"display_desc":"360P","superscript":"","codecs":[],"format":"mp4","description":"流畅 360P","quality":16,"new_description":"360P 流畅"}],"message":"","accept_quality":[64,16],"quality":16,"timelength":180203,"has_paid":false,"clip_info_list":[{"materialNo":0,"start":15,"end":21,"toastText":"即将跳过片头","clipType":"CLIP_TYPE_OP"},{"materialNo":0,"start":151,"end":180,"toastText":"即将跳过片尾","clipType":"CLIP_TYPE_ED"}],"accept_description":["高清 720P","流畅 360P"],"status":2}
     */

    private int code;
    private String message;
    private ResultBean result;

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

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable {
        /**
         * accept_format : mp4720,mp4
         * code : 0
         * seek_param : start
         * is_preview : 0
         * fnval : 0
         * video_project : true
         * fnver : 0
         * type : MP4
         * bp : 0
         * result : suee
         * seek_type : second
         * from : local
         * video_codecid : 7
         * record_info : {"record_icon":"","record":""}
         * durl : [{"size":10413610,"ahead":"","length":180203,"vhead":"","backup_url":["https://upos-sz-mirrorali.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671275626&gen=playurlv2&os=alibv&oi=1935005709&trid=1f36b0fd30834028bb741ce567e5a5c3p&mid=0&platform=pc&upsig=bb5977ad409d5a92693fe6e3e14c43b1&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=1&bw=57853&logo=40000000","https://upos-sz-mirroralib.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671275626&gen=playurlv2&os=alibbv&oi=1935005709&trid=1f36b0fd30834028bb741ce567e5a5c3p&mid=0&platform=pc&upsig=705db58f0fa5755e11e8cbbcd0d7de5c&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=1&bw=57853&logo=40000000"],"url":"https://upos-sz-estgoss.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671275626&gen=playurlv2&os=upos&oi=1935005709&trid=1f36b0fd30834028bb741ce567e5a5c3p&mid=0&platform=pc&upsig=27779ca7cba1c05977ef4db7d6dee1fc&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=1&bw=57853&logo=80000000","order":1,"md5":""}]
         * is_drm : false
         * no_rexcode : 0
         * format : mp4
         * support_formats : [{"display_desc":"720P","superscript":"","need_login":true,"codecs":[],"format":"mp4720","description":"高清 720P","quality":64,"new_description":"720P 高清"},{"display_desc":"360P","superscript":"","codecs":[],"format":"mp4","description":"流畅 360P","quality":16,"new_description":"360P 流畅"}]
         * message :
         * accept_quality : [64,16]
         * quality : 16
         * timelength : 180203
         * has_paid : false
         * clip_info_list : [{"materialNo":0,"start":15,"end":21,"toastText":"即将跳过片头","clipType":"CLIP_TYPE_OP"},{"materialNo":0,"start":151,"end":180,"toastText":"即将跳过片尾","clipType":"CLIP_TYPE_ED"}]
         * accept_description : ["高清 720P","流畅 360P"]
         * status : 2
         */

        private String accept_format;
        private int code;
        private String seek_param;
        private int is_preview;
        private int fnval;
        private boolean video_project;
        private int fnver;
        private String type;
        private int bp;
        private String result;
        private String seek_type;
        private String from;
        private int video_codecid;
        private RecordInfoBean record_info;
        private boolean is_drm;
        private int no_rexcode;
        private String format;
        private String message;
        private int quality;
        private int timelength;
        private boolean has_paid;
        private int status;
        private List<DurlBean> durl;
        private List<SupportFormatsBean> support_formats;
        private List<Integer> accept_quality;
        private List<ClipInfoListBean> clip_info_list;
        private List<String> accept_description;

        public String getAccept_format() {
            return accept_format;
        }

        public void setAccept_format(String accept_format) {
            this.accept_format = accept_format;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getSeek_param() {
            return seek_param;
        }

        public void setSeek_param(String seek_param) {
            this.seek_param = seek_param;
        }

        public int getIs_preview() {
            return is_preview;
        }

        public void setIs_preview(int is_preview) {
            this.is_preview = is_preview;
        }

        public int getFnval() {
            return fnval;
        }

        public void setFnval(int fnval) {
            this.fnval = fnval;
        }

        public boolean isVideo_project() {
            return video_project;
        }

        public void setVideo_project(boolean video_project) {
            this.video_project = video_project;
        }

        public int getFnver() {
            return fnver;
        }

        public void setFnver(int fnver) {
            this.fnver = fnver;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getBp() {
            return bp;
        }

        public void setBp(int bp) {
            this.bp = bp;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getSeek_type() {
            return seek_type;
        }

        public void setSeek_type(String seek_type) {
            this.seek_type = seek_type;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public int getVideo_codecid() {
            return video_codecid;
        }

        public void setVideo_codecid(int video_codecid) {
            this.video_codecid = video_codecid;
        }

        public RecordInfoBean getRecord_info() {
            return record_info;
        }

        public void setRecord_info(RecordInfoBean record_info) {
            this.record_info = record_info;
        }

        public boolean isIs_drm() {
            return is_drm;
        }

        public void setIs_drm(boolean is_drm) {
            this.is_drm = is_drm;
        }

        public int getNo_rexcode() {
            return no_rexcode;
        }

        public void setNo_rexcode(int no_rexcode) {
            this.no_rexcode = no_rexcode;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
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

        public int getTimelength() {
            return timelength;
        }

        public void setTimelength(int timelength) {
            this.timelength = timelength;
        }

        public boolean isHas_paid() {
            return has_paid;
        }

        public void setHas_paid(boolean has_paid) {
            this.has_paid = has_paid;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
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

        public List<Integer> getAccept_quality() {
            return accept_quality;
        }

        public void setAccept_quality(List<Integer> accept_quality) {
            this.accept_quality = accept_quality;
        }

        public List<ClipInfoListBean> getClip_info_list() {
            return clip_info_list;
        }

        public void setClip_info_list(List<ClipInfoListBean> clip_info_list) {
            this.clip_info_list = clip_info_list;
        }

        public List<String> getAccept_description() {
            return accept_description;
        }

        public void setAccept_description(List<String> accept_description) {
            this.accept_description = accept_description;
        }

        public static class RecordInfoBean implements Serializable {
            /**
             * record_icon :
             * record :
             */

            private String record_icon;
            private String record;

            public String getRecord_icon() {
                return record_icon;
            }

            public void setRecord_icon(String record_icon) {
                this.record_icon = record_icon;
            }

            public String getRecord() {
                return record;
            }

            public void setRecord(String record) {
                this.record = record;
            }
        }

        public static class DurlBean implements Serializable {
            /**
             * size : 10413610
             * ahead :
             * length : 180203
             * vhead :
             * backup_url : ["https://upos-sz-mirrorali.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671275626&gen=playurlv2&os=alibv&oi=1935005709&trid=1f36b0fd30834028bb741ce567e5a5c3p&mid=0&platform=pc&upsig=bb5977ad409d5a92693fe6e3e14c43b1&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=1&bw=57853&logo=40000000","https://upos-sz-mirroralib.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671275626&gen=playurlv2&os=alibbv&oi=1935005709&trid=1f36b0fd30834028bb741ce567e5a5c3p&mid=0&platform=pc&upsig=705db58f0fa5755e11e8cbbcd0d7de5c&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=1&bw=57853&logo=40000000"]
             * url : https://upos-sz-estgoss.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671275626&gen=playurlv2&os=upos&oi=1935005709&trid=1f36b0fd30834028bb741ce567e5a5c3p&mid=0&platform=pc&upsig=27779ca7cba1c05977ef4db7d6dee1fc&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=1&bw=57853&logo=80000000
             * order : 1
             * md5 :
             */

            private Long size;
            private String ahead;
            private int length;
            private String vhead;
            private String url;
            private int order;
            private String md5;
            private List<String> backup_url;

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

            public int getLength() {
                return length;
            }

            public void setLength(int length) {
                this.length = length;
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

            public int getOrder() {
                return order;
            }

            public void setOrder(int order) {
                this.order = order;
            }

            public String getMd5() {
                return md5;
            }

            public void setMd5(String md5) {
                this.md5 = md5;
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
             * display_desc : 720P
             * superscript :
             * need_login : true
             * codecs : []
             * format : mp4720
             * description : 高清 720P
             * quality : 64
             * new_description : 720P 高清
             */

            private String display_desc;
            private String superscript;
            private boolean need_login;
            private String format;
            private String description;
            private int quality;
            private String new_description;
            private List<?> codecs;

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

            public boolean isNeed_login() {
                return need_login;
            }

            public void setNeed_login(boolean need_login) {
                this.need_login = need_login;
            }

            public String getFormat() {
                return format;
            }

            public void setFormat(String format) {
                this.format = format;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getQuality() {
                return quality;
            }

            public void setQuality(int quality) {
                this.quality = quality;
            }

            public String getNew_description() {
                return new_description;
            }

            public void setNew_description(String new_description) {
                this.new_description = new_description;
            }

            public List<?> getCodecs() {
                return codecs;
            }

            public void setCodecs(List<?> codecs) {
                this.codecs = codecs;
            }
        }

        public static class ClipInfoListBean implements Serializable {
            /**
             * materialNo : 0
             * start : 15
             * end : 21
             * toastText : 即将跳过片头
             * clipType : CLIP_TYPE_OP
             */

            private int materialNo;
            private int start;
            private int end;
            private String toastText;
            private String clipType;

            public int getMaterialNo() {
                return materialNo;
            }

            public void setMaterialNo(int materialNo) {
                this.materialNo = materialNo;
            }

            public int getStart() {
                return start;
            }

            public void setStart(int start) {
                this.start = start;
            }

            public int getEnd() {
                return end;
            }

            public void setEnd(int end) {
                this.end = end;
            }

            public String getToastText() {
                return toastText;
            }

            public void setToastText(String toastText) {
                this.toastText = toastText;
            }

            public String getClipType() {
                return clipType;
            }

            public void setClipType(String clipType) {
                this.clipType = clipType;
            }
        }
    }
}
