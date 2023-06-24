package com.imcys.bilibilias.home.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author:imcys
 * @create: 2022-11-13 22:32
 * @Description:
 */
public class VideoBaseBean implements Serializable {


    /**
     * code : 0
     * message : 0
     * ttl : 1
     * data : {"bvid":"BV1Sd4y1y7A7","aid":389458988,"videos":1,"tid":27,"tname":"综合","copyright":1,"pic":"http://i2.hdslb.com/bfs/archive/fe56fbc58cf12ba66eefba82041d7d1f68778cfd.jpg","title":"\u201c从前，我以为这只是一部动画片而已\u201d","pubdate":1666877362,"ctime":1666877363,"desc":"BGM：LIKPIA - 夏·烟火","desc_v2":[{"raw_text":"BGM：LIKPIA - 夏·烟火","type":1,"biz_id":0}],"state":0,"duration":464,"mission_id":1021837,"rights":{"bp":0,"elec":0,"download":1,"movie":0,"pay":0,"hd5":0,"no_reprint":1,"autoplay":1,"ugc_pay":0,"is_cooperation":0,"ugc_pay_preview":0,"no_background":0,"clean_mode":0,"is_stein_gate":0,"is_360":0,"no_share":0,"arc_pay":0,"free_watch":0},"owner":{"mid":397857697,"name":"黄方块和粉星星a","face":"https://i1.hdslb.com/bfs/face/e6e5adfb421686da23f3f14103cd88673030e088.jpg"},"stat":{"aid":389458988,"view":1679675,"danmaku":756,"reply":772,"favorite":75754,"coin":20967,"share":9639,"now_rank":0,"his_rank":0,"like":92530,"dislike":0,"evaluation":"","argue_msg":""},"dynamic":"","cid":873650644,"dimension":{"width":1440,"height":1080,"rotate":0},"premiere":null,"teenage_mode":0,"is_chargeable_season":false,"is_story":false,"no_cache":false,"pages":[{"cid":873650644,"page":1,"from":"vupload","part":"1666875071332.mp4","duration":464,"vid":"","weblink":"","dimension":{"width":1440,"height":1080,"rotate":0},"first_frame":"http://i2.hdslb.com/bfs/storyff/n221027a21ph6uu8ui2xn310a41s8hf9_firsti.jpg"}],"subtitle":{"allow_submit":false,"list":[{"id":1079830025387542784,"lan":"ai-zh","lan_doc":"中文（自动生成）","is_lock":false,"subtitle_url":"http://i0.hdslb.com/bfs/ai_subtitle/prod/3894589888736506442d117b7417b54c4df6c5915e2b79c7f8","type":1,"id_str":"1079830025387542784","ai_type":0,"ai_status":2,"author":{"mid":0,"name":"","sex":"","face":"","sign":"","rank":0,"birthday":0,"is_fake_account":0,"is_deleted":0,"in_reg_audit":0,"is_senior_member":0}}]},"is_season_display":false,"user_garb":{"url_image_ani_cut":""},"honor_reply":{"honor":[{"aid":389458988,"type":4,"desc":"热门","weekly_recommend_num":0}]},"like_icon":""}
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
         * bvid : BV1Sd4y1y7A7
         * aid : 389458988
         * videos : 1
         * tid : 27
         * tname : 综合
         * copyright : 1
         * pic : http://i2.hdslb.com/bfs/archive/fe56fbc58cf12ba66eefba82041d7d1f68778cfd.jpg
         * title : “从前，我以为这只是一部动画片而已”
         * pubdate : 1666877362
         * ctime : 1666877363
         * desc : BGM：LIKPIA - 夏·烟火
         * desc_v2 : [{"raw_text":"BGM：LIKPIA - 夏·烟火","type":1,"biz_id":0}]
         * state : 0
         * duration : 464
         * mission_id : 1021837
         * rights : {"bp":0,"elec":0,"download":1,"movie":0,"pay":0,"hd5":0,"no_reprint":1,"autoplay":1,"ugc_pay":0,"is_cooperation":0,"ugc_pay_preview":0,"no_background":0,"clean_mode":0,"is_stein_gate":0,"is_360":0,"no_share":0,"arc_pay":0,"free_watch":0}
         * owner : {"mid":397857697,"name":"黄方块和粉星星a","face":"https://i1.hdslb.com/bfs/face/e6e5adfb421686da23f3f14103cd88673030e088.jpg"}
         * stat : {"aid":389458988,"view":1679675,"danmaku":756,"reply":772,"favorite":75754,"coin":20967,"share":9639,"now_rank":0,"his_rank":0,"like":92530,"dislike":0,"evaluation":"","argue_msg":""}
         * dynamic :
         * cid : 873650644
         * dimension : {"width":1440,"height":1080,"rotate":0}
         * premiere : null
         * teenage_mode : 0
         * is_chargeable_season : false
         * is_story : false
         * no_cache : false
         * pages : [{"cid":873650644,"page":1,"from":"vupload","part":"1666875071332.mp4","duration":464,"vid":"","weblink":"","dimension":{"width":1440,"height":1080,"rotate":0},"first_frame":"http://i2.hdslb.com/bfs/storyff/n221027a21ph6uu8ui2xn310a41s8hf9_firsti.jpg"}]
         * subtitle : {"allow_submit":false,"list":[{"id":1079830025387542784,"lan":"ai-zh","lan_doc":"中文（自动生成）","is_lock":false,"subtitle_url":"http://i0.hdslb.com/bfs/ai_subtitle/prod/3894589888736506442d117b7417b54c4df6c5915e2b79c7f8","type":1,"id_str":"1079830025387542784","ai_type":0,"ai_status":2,"author":{"mid":0,"name":"","sex":"","face":"","sign":"","rank":0,"birthday":0,"is_fake_account":0,"is_deleted":0,"in_reg_audit":0,"is_senior_member":0}}]}
         * is_season_display : false
         * user_garb : {"url_image_ani_cut":""}
         * honor_reply : {"honor":[{"aid":389458988,"type":4,"desc":"热门","weekly_recommend_num":0}]}
         * like_icon :
         */

        private String bvid;
        private int aid;
        private int videos;
        private int tid;
        private String tname;
        private int copyright;
        private String pic;
        private String title;
        private int pubdate;
        private int ctime;
        private String desc;
        private int state;
        private int duration;
        private int mission_id;
        private RightsBean rights;
        private OwnerBean owner;
        private StatBean stat;
        private String dynamic;
        private int cid;
        private DimensionBean dimension;
        private Object premiere;
        private int teenage_mode;
        private boolean is_chargeable_season;
        private boolean is_story;
        private boolean no_cache;
        private SubtitleBean subtitle;
        private boolean is_season_display;
        private UserGarbBean user_garb;
        private HonorReplyBean honor_reply;
        private String like_icon;
        private List<DescV2Bean> desc_v2;
        private List<PagesBean> pages;
        private String redirect_url;

        public String getRedirect_url() {
            return redirect_url;
        }

        public void setRedirect_url(String redirect_url) {
            this.redirect_url = redirect_url;
        }

        public String getBvid() {
            return bvid;
        }

        public void setBvid(String bvid) {
            this.bvid = bvid;
        }

        public int getAid() {
            return aid;
        }

        public void setAid(int aid) {
            this.aid = aid;
        }

        public int getVideos() {
            return videos;
        }

        public void setVideos(int videos) {
            this.videos = videos;
        }

        public int getTid() {
            return tid;
        }

        public void setTid(int tid) {
            this.tid = tid;
        }

        public String getTname() {
            return tname;
        }

        public void setTname(String tname) {
            this.tname = tname;
        }

        public int getCopyright() {
            return copyright;
        }

        public void setCopyright(int copyright) {
            this.copyright = copyright;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getPubdate() {
            return pubdate;
        }

        public void setPubdate(int pubdate) {
            this.pubdate = pubdate;
        }

        public int getCtime() {
            return ctime;
        }

        public void setCtime(int ctime) {
            this.ctime = ctime;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getMission_id() {
            return mission_id;
        }

        public void setMission_id(int mission_id) {
            this.mission_id = mission_id;
        }

        public RightsBean getRights() {
            return rights;
        }

        public void setRights(RightsBean rights) {
            this.rights = rights;
        }

        public OwnerBean getOwner() {
            return owner;
        }

        public void setOwner(OwnerBean owner) {
            this.owner = owner;
        }

        public StatBean getStat() {
            return stat;
        }

        public void setStat(StatBean stat) {
            this.stat = stat;
        }

        public String getDynamic() {
            return dynamic;
        }

        public void setDynamic(String dynamic) {
            this.dynamic = dynamic;
        }

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public DimensionBean getDimension() {
            return dimension;
        }

        public void setDimension(DimensionBean dimension) {
            this.dimension = dimension;
        }

        public Object getPremiere() {
            return premiere;
        }

        public void setPremiere(Object premiere) {
            this.premiere = premiere;
        }

        public int getTeenage_mode() {
            return teenage_mode;
        }

        public void setTeenage_mode(int teenage_mode) {
            this.teenage_mode = teenage_mode;
        }

        public boolean isIs_chargeable_season() {
            return is_chargeable_season;
        }

        public void setIs_chargeable_season(boolean is_chargeable_season) {
            this.is_chargeable_season = is_chargeable_season;
        }

        public boolean isIs_story() {
            return is_story;
        }

        public void setIs_story(boolean is_story) {
            this.is_story = is_story;
        }

        public boolean isNo_cache() {
            return no_cache;
        }

        public void setNo_cache(boolean no_cache) {
            this.no_cache = no_cache;
        }

        public SubtitleBean getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(SubtitleBean subtitle) {
            this.subtitle = subtitle;
        }

        public boolean isIs_season_display() {
            return is_season_display;
        }

        public void setIs_season_display(boolean is_season_display) {
            this.is_season_display = is_season_display;
        }

        public UserGarbBean getUser_garb() {
            return user_garb;
        }

        public void setUser_garb(UserGarbBean user_garb) {
            this.user_garb = user_garb;
        }

        public HonorReplyBean getHonor_reply() {
            return honor_reply;
        }

        public void setHonor_reply(HonorReplyBean honor_reply) {
            this.honor_reply = honor_reply;
        }

        public String getLike_icon() {
            return like_icon;
        }

        public void setLike_icon(String like_icon) {
            this.like_icon = like_icon;
        }

        public List<DescV2Bean> getDesc_v2() {
            return desc_v2;
        }

        public void setDesc_v2(List<DescV2Bean> desc_v2) {
            this.desc_v2 = desc_v2;
        }

        public List<PagesBean> getPages() {
            return pages;
        }

        public void setPages(List<PagesBean> pages) {
            this.pages = pages;
        }

        public static class RightsBean implements Serializable {
            /**
             * bp : 0
             * elec : 0
             * download : 1
             * movie : 0
             * pay : 0
             * hd5 : 0
             * no_reprint : 1
             * autoplay : 1
             * ugc_pay : 0
             * is_cooperation : 0
             * ugc_pay_preview : 0
             * no_background : 0
             * clean_mode : 0
             * is_stein_gate : 0
             * is_360 : 0
             * no_share : 0
             * arc_pay : 0
             * free_watch : 0
             */

            private int bp;
            private int elec;
            private int download;
            private int movie;
            private int pay;
            private int hd5;
            private int no_reprint;
            private int autoplay;
            private int ugc_pay;
            private int is_cooperation;
            private int ugc_pay_preview;
            private int no_background;
            private int clean_mode;
            private int is_stein_gate;
            private int is_360;
            private int no_share;
            private int arc_pay;
            private int free_watch;

            public int getBp() {
                return bp;
            }

            public void setBp(int bp) {
                this.bp = bp;
            }

            public int getElec() {
                return elec;
            }

            public void setElec(int elec) {
                this.elec = elec;
            }

            public int getDownload() {
                return download;
            }

            public void setDownload(int download) {
                this.download = download;
            }

            public int getMovie() {
                return movie;
            }

            public void setMovie(int movie) {
                this.movie = movie;
            }

            public int getPay() {
                return pay;
            }

            public void setPay(int pay) {
                this.pay = pay;
            }

            public int getHd5() {
                return hd5;
            }

            public void setHd5(int hd5) {
                this.hd5 = hd5;
            }

            public int getNo_reprint() {
                return no_reprint;
            }

            public void setNo_reprint(int no_reprint) {
                this.no_reprint = no_reprint;
            }

            public int getAutoplay() {
                return autoplay;
            }

            public void setAutoplay(int autoplay) {
                this.autoplay = autoplay;
            }

            public int getUgc_pay() {
                return ugc_pay;
            }

            public void setUgc_pay(int ugc_pay) {
                this.ugc_pay = ugc_pay;
            }

            public int getIs_cooperation() {
                return is_cooperation;
            }

            public void setIs_cooperation(int is_cooperation) {
                this.is_cooperation = is_cooperation;
            }

            public int getUgc_pay_preview() {
                return ugc_pay_preview;
            }

            public void setUgc_pay_preview(int ugc_pay_preview) {
                this.ugc_pay_preview = ugc_pay_preview;
            }

            public int getNo_background() {
                return no_background;
            }

            public void setNo_background(int no_background) {
                this.no_background = no_background;
            }

            public int getClean_mode() {
                return clean_mode;
            }

            public void setClean_mode(int clean_mode) {
                this.clean_mode = clean_mode;
            }

            public int getIs_stein_gate() {
                return is_stein_gate;
            }

            public void setIs_stein_gate(int is_stein_gate) {
                this.is_stein_gate = is_stein_gate;
            }

            public int getIs_360() {
                return is_360;
            }

            public void setIs_360(int is_360) {
                this.is_360 = is_360;
            }

            public int getNo_share() {
                return no_share;
            }

            public void setNo_share(int no_share) {
                this.no_share = no_share;
            }

            public int getArc_pay() {
                return arc_pay;
            }

            public void setArc_pay(int arc_pay) {
                this.arc_pay = arc_pay;
            }

            public int getFree_watch() {
                return free_watch;
            }

            public void setFree_watch(int free_watch) {
                this.free_watch = free_watch;
            }
        }

        public static class OwnerBean implements Serializable {
            /**
             * mid : 397857697
             * name : 黄方块和粉星星a
             * face : https://i1.hdslb.com/bfs/face/e6e5adfb421686da23f3f14103cd88673030e088.jpg
             */

            private long mid;
            private String name;
            private String face;

            public long getMid() {
                return mid;
            }

            public void setMid(long mid) {
                this.mid = mid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getFace() {
                return face;
            }

            public void setFace(String face) {
                this.face = face;
            }
        }

        public static class StatBean implements Serializable {
            /**
             * aid : 389458988
             * view : 1679675
             * danmaku : 756
             * reply : 772
             * favorite : 75754
             * coin : 20967
             * share : 9639
             * now_rank : 0
             * his_rank : 0
             * like : 92530
             * dislike : 0
             * evaluation :
             * argue_msg :
             */

            private int aid;
            private int view;
            private int danmaku;
            private int reply;
            private int favorite;
            private int coin;
            private int share;
            private int now_rank;
            private int his_rank;
            private int like;
            private int dislike;
            private String evaluation;
            private String argue_msg;

            public int getAid() {
                return aid;
            }

            public void setAid(int aid) {
                this.aid = aid;
            }

            public int getView() {
                return view;
            }

            public void setView(int view) {
                this.view = view;
            }

            public int getDanmaku() {
                return danmaku;
            }

            public void setDanmaku(int danmaku) {
                this.danmaku = danmaku;
            }

            public int getReply() {
                return reply;
            }

            public void setReply(int reply) {
                this.reply = reply;
            }

            public int getFavorite() {
                return favorite;
            }

            public void setFavorite(int favorite) {
                this.favorite = favorite;
            }

            public int getCoin() {
                return coin;
            }

            public void setCoin(int coin) {
                this.coin = coin;
            }

            public int getShare() {
                return share;
            }

            public void setShare(int share) {
                this.share = share;
            }

            public int getNow_rank() {
                return now_rank;
            }

            public void setNow_rank(int now_rank) {
                this.now_rank = now_rank;
            }

            public int getHis_rank() {
                return his_rank;
            }

            public void setHis_rank(int his_rank) {
                this.his_rank = his_rank;
            }

            public int getLike() {
                return like;
            }

            public void setLike(int like) {
                this.like = like;
            }

            public int getDislike() {
                return dislike;
            }

            public void setDislike(int dislike) {
                this.dislike = dislike;
            }

            public String getEvaluation() {
                return evaluation;
            }

            public void setEvaluation(String evaluation) {
                this.evaluation = evaluation;
            }

            public String getArgue_msg() {
                return argue_msg;
            }

            public void setArgue_msg(String argue_msg) {
                this.argue_msg = argue_msg;
            }
        }

        public static class DimensionBean implements Serializable {
            /**
             * width : 1440
             * height : 1080
             * rotate : 0
             */

            private int width;
            private int height;
            private int rotate;

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getRotate() {
                return rotate;
            }

            public void setRotate(int rotate) {
                this.rotate = rotate;
            }
        }

        public static class SubtitleBean implements Serializable {
            /**
             * allow_submit : false
             * list : [{"id":1079830025387542784,"lan":"ai-zh","lan_doc":"中文（自动生成）","is_lock":false,"subtitle_url":"http://i0.hdslb.com/bfs/ai_subtitle/prod/3894589888736506442d117b7417b54c4df6c5915e2b79c7f8","type":1,"id_str":"1079830025387542784","ai_type":0,"ai_status":2,"author":{"mid":0,"name":"","sex":"","face":"","sign":"","rank":0,"birthday":0,"is_fake_account":0,"is_deleted":0,"in_reg_audit":0,"is_senior_member":0}}]
             */

            private boolean allow_submit;
            private List<ListBean> list;

            public boolean isAllow_submit() {
                return allow_submit;
            }

            public void setAllow_submit(boolean allow_submit) {
                this.allow_submit = allow_submit;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class ListBean implements Serializable {
                /**
                 * id : 1079830025387542784
                 * lan : ai-zh
                 * lan_doc : 中文（自动生成）
                 * is_lock : false
                 * subtitle_url : http://i0.hdslb.com/bfs/ai_subtitle/prod/3894589888736506442d117b7417b54c4df6c5915e2b79c7f8
                 * type : 1
                 * id_str : 1079830025387542784
                 * ai_type : 0
                 * ai_status : 2
                 * author : {"mid":0,"name":"","sex":"","face":"","sign":"","rank":0,"birthday":0,"is_fake_account":0,"is_deleted":0,"in_reg_audit":0,"is_senior_member":0}
                 */

                private long id;
                private String lan;
                private String lan_doc;
                private boolean is_lock;
                private String subtitle_url;
                private int type;
                private String id_str;
                private int ai_type;
                private int ai_status;
                private AuthorBean author;

                public long getId() {
                    return id;
                }

                public void setId(long id) {
                    this.id = id;
                }

                public String getLan() {
                    return lan;
                }

                public void setLan(String lan) {
                    this.lan = lan;
                }

                public String getLan_doc() {
                    return lan_doc;
                }

                public void setLan_doc(String lan_doc) {
                    this.lan_doc = lan_doc;
                }

                public boolean isIs_lock() {
                    return is_lock;
                }

                public void setIs_lock(boolean is_lock) {
                    this.is_lock = is_lock;
                }

                public String getSubtitle_url() {
                    return subtitle_url;
                }

                public void setSubtitle_url(String subtitle_url) {
                    this.subtitle_url = subtitle_url;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public String getId_str() {
                    return id_str;
                }

                public void setId_str(String id_str) {
                    this.id_str = id_str;
                }

                public int getAi_type() {
                    return ai_type;
                }

                public void setAi_type(int ai_type) {
                    this.ai_type = ai_type;
                }

                public int getAi_status() {
                    return ai_status;
                }

                public void setAi_status(int ai_status) {
                    this.ai_status = ai_status;
                }

                public AuthorBean getAuthor() {
                    return author;
                }

                public void setAuthor(AuthorBean author) {
                    this.author = author;
                }

                public static class AuthorBean {
                    /**
                     * mid : 0
                     * name :
                     * sex :
                     * face :
                     * sign :
                     * rank : 0
                     * birthday : 0
                     * is_fake_account : 0
                     * is_deleted : 0
                     * in_reg_audit : 0
                     * is_senior_member : 0
                     */

                    private int mid;
                    private String name;
                    private String sex;
                    private String face;
                    private String sign;
                    private int rank;
                    private int birthday;
                    private int is_fake_account;
                    private int is_deleted;
                    private int in_reg_audit;
                    private int is_senior_member;

                    public int getMid() {
                        return mid;
                    }

                    public void setMid(int mid) {
                        this.mid = mid;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getSex() {
                        return sex;
                    }

                    public void setSex(String sex) {
                        this.sex = sex;
                    }

                    public String getFace() {
                        return face;
                    }

                    public void setFace(String face) {
                        this.face = face;
                    }

                    public String getSign() {
                        return sign;
                    }

                    public void setSign(String sign) {
                        this.sign = sign;
                    }

                    public int getRank() {
                        return rank;
                    }

                    public void setRank(int rank) {
                        this.rank = rank;
                    }

                    public int getBirthday() {
                        return birthday;
                    }

                    public void setBirthday(int birthday) {
                        this.birthday = birthday;
                    }

                    public int getIs_fake_account() {
                        return is_fake_account;
                    }

                    public void setIs_fake_account(int is_fake_account) {
                        this.is_fake_account = is_fake_account;
                    }

                    public int getIs_deleted() {
                        return is_deleted;
                    }

                    public void setIs_deleted(int is_deleted) {
                        this.is_deleted = is_deleted;
                    }

                    public int getIn_reg_audit() {
                        return in_reg_audit;
                    }

                    public void setIn_reg_audit(int in_reg_audit) {
                        this.in_reg_audit = in_reg_audit;
                    }

                    public int getIs_senior_member() {
                        return is_senior_member;
                    }

                    public void setIs_senior_member(int is_senior_member) {
                        this.is_senior_member = is_senior_member;
                    }
                }
            }
        }

        public static class UserGarbBean implements Serializable {
            /**
             * url_image_ani_cut :
             */

            private String url_image_ani_cut;

            public String getUrl_image_ani_cut() {
                return url_image_ani_cut;
            }

            public void setUrl_image_ani_cut(String url_image_ani_cut) {
                this.url_image_ani_cut = url_image_ani_cut;
            }
        }

        public static class HonorReplyBean implements Serializable {
            private List<HonorBean> honor;

            public List<HonorBean> getHonor() {
                return honor;
            }

            public void setHonor(List<HonorBean> honor) {
                this.honor = honor;
            }

            public static class HonorBean implements Serializable {
                /**
                 * aid : 389458988
                 * type : 4
                 * desc : 热门
                 * weekly_recommend_num : 0
                 */

                private int aid;
                private int type;
                private String desc;
                private int weekly_recommend_num;

                public int getAid() {
                    return aid;
                }

                public void setAid(int aid) {
                    this.aid = aid;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public String getDesc() {
                    return desc;
                }

                public void setDesc(String desc) {
                    this.desc = desc;
                }

                public int getWeekly_recommend_num() {
                    return weekly_recommend_num;
                }

                public void setWeekly_recommend_num(int weekly_recommend_num) {
                    this.weekly_recommend_num = weekly_recommend_num;
                }
            }
        }

        public static class DescV2Bean implements Serializable {
            /**
             * raw_text : BGM：LIKPIA - 夏·烟火
             * type : 1
             * biz_id : 0
             */

            private String raw_text;
            private int type;
            private int biz_id;

            public String getRaw_text() {
                return raw_text;
            }

            public void setRaw_text(String raw_text) {
                this.raw_text = raw_text;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getBiz_id() {
                return biz_id;
            }

            public void setBiz_id(int biz_id) {
                this.biz_id = biz_id;
            }
        }

        public static class PagesBean implements Serializable {
            /**
             * cid : 873650644
             * page : 1
             * from : vupload
             * part : 1666875071332.mp4
             * duration : 464
             * vid :
             * weblink :
             * dimension : {"width":1440,"height":1080,"rotate":0}
             * first_frame : http://i2.hdslb.com/bfs/storyff/n221027a21ph6uu8ui2xn310a41s8hf9_firsti.jpg
             */

            private int cid;
            private int page;
            private String from;
            private String part;
            private int duration;
            private String vid;
            private String weblink;
            private DimensionBeanX dimension;
            private String first_frame;

            public int getCid() {
                return cid;
            }

            public void setCid(int cid) {
                this.cid = cid;
            }

            public int getPage() {
                return page;
            }

            public void setPage(int page) {
                this.page = page;
            }

            public String getFrom() {
                return from;
            }

            public void setFrom(String from) {
                this.from = from;
            }

            public String getPart() {
                return part;
            }

            public void setPart(String part) {
                this.part = part;
            }

            public int getDuration() {
                return duration;
            }

            public void setDuration(int duration) {
                this.duration = duration;
            }

            public String getVid() {
                return vid;
            }

            public void setVid(String vid) {
                this.vid = vid;
            }

            public String getWeblink() {
                return weblink;
            }

            public void setWeblink(String weblink) {
                this.weblink = weblink;
            }

            public DimensionBeanX getDimension() {
                return dimension;
            }

            public void setDimension(DimensionBeanX dimension) {
                this.dimension = dimension;
            }

            public String getFirst_frame() {
                return first_frame;
            }

            public void setFirst_frame(String first_frame) {
                this.first_frame = first_frame;
            }

            public static class DimensionBeanX implements Serializable {
                /**
                 * width : 1440
                 * height : 1080
                 * rotate : 0
                 */

                private int width;
                private int height;
                private int rotate;

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public int getRotate() {
                    return rotate;
                }

                public void setRotate(int rotate) {
                    this.rotate = rotate;
                }
            }
        }
    }
}
