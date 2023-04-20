package com.imcys.bilibilias.home.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author:imcys
 * @create: 2022-12-17 15:19
 * @Description: 番剧剧集明细
 */
public class BangumiSeasonBean implements Serializable {

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

    public static class ResultBean implements Serializable{

        private ActivityBean activity;
        private String alias;
        private String bkg_cover;
        private String cover;
        private String evaluate;
        private FreyaBean freya;
        private String jp_title;
        private String link;
        private int media_id;
        private int mode;
        private NewEpBean new_ep;
        private PaymentBean payment;
        private PositiveBean positive;
        private PublishBean publish;
        private RatingBean rating;
        private String record;
        private RightsBean rights;
        private int season_id;
        private String season_title;
        private SeriesBean series;

        private List<SectionBean> section;
        private String share_copy;
        private String share_sub_title;
        private String share_url;
        private ShowBean show;
        private int show_season_type;
        private String square_cover;
        private StatBean stat;
        private int status;
        private String subtitle;
        private String title;
        private int total;
        private int type;
        private UpInfoBean up_info;
        private UserStatusBean user_status;
        private List<AreasBean> areas;
        private List<EpisodesBean> episodes;

        public List<SectionBean> getSection() {
            return section;
        }

        public void setSection(List<SectionBean> section) {
            this.section = section;
        }

        public ActivityBean getActivity() {
            return activity;
        }

        public void setActivity(ActivityBean activity) {
            this.activity = activity;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getBkg_cover() {
            return bkg_cover;
        }

        public void setBkg_cover(String bkg_cover) {
            this.bkg_cover = bkg_cover;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getEvaluate() {
            return evaluate;
        }

        public void setEvaluate(String evaluate) {
            this.evaluate = evaluate;
        }

        public FreyaBean getFreya() {
            return freya;
        }

        public void setFreya(FreyaBean freya) {
            this.freya = freya;
        }

        public String getJp_title() {
            return jp_title;
        }

        public void setJp_title(String jp_title) {
            this.jp_title = jp_title;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public int getMedia_id() {
            return media_id;
        }

        public void setMedia_id(int media_id) {
            this.media_id = media_id;
        }

        public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

        public NewEpBean getNew_ep() {
            return new_ep;
        }

        public void setNew_ep(NewEpBean new_ep) {
            this.new_ep = new_ep;
        }

        public PaymentBean getPayment() {
            return payment;
        }

        public void setPayment(PaymentBean payment) {
            this.payment = payment;
        }

        public PositiveBean getPositive() {
            return positive;
        }

        public void setPositive(PositiveBean positive) {
            this.positive = positive;
        }

        public PublishBean getPublish() {
            return publish;
        }

        public void setPublish(PublishBean publish) {
            this.publish = publish;
        }

        public RatingBean getRating() {
            return rating;
        }

        public void setRating(RatingBean rating) {
            this.rating = rating;
        }

        public String getRecord() {
            return record;
        }

        public void setRecord(String record) {
            this.record = record;
        }

        public RightsBean getRights() {
            return rights;
        }

        public void setRights(RightsBean rights) {
            this.rights = rights;
        }

        public int getSeason_id() {
            return season_id;
        }

        public void setSeason_id(int season_id) {
            this.season_id = season_id;
        }

        public String getSeason_title() {
            return season_title;
        }

        public void setSeason_title(String season_title) {
            this.season_title = season_title;
        }

        public SeriesBean getSeries() {
            return series;
        }

        public void setSeries(SeriesBean series) {
            this.series = series;
        }

        public String getShare_copy() {
            return share_copy;
        }

        public void setShare_copy(String share_copy) {
            this.share_copy = share_copy;
        }

        public String getShare_sub_title() {
            return share_sub_title;
        }

        public void setShare_sub_title(String share_sub_title) {
            this.share_sub_title = share_sub_title;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public ShowBean getShow() {
            return show;
        }

        public void setShow(ShowBean show) {
            this.show = show;
        }

        public int getShow_season_type() {
            return show_season_type;
        }

        public void setShow_season_type(int show_season_type) {
            this.show_season_type = show_season_type;
        }

        public String getSquare_cover() {
            return square_cover;
        }

        public void setSquare_cover(String square_cover) {
            this.square_cover = square_cover;
        }

        public StatBean getStat() {
            return stat;
        }

        public void setStat(StatBean stat) {
            this.stat = stat;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public UpInfoBean getUp_info() {
            return up_info;
        }

        public void setUp_info(UpInfoBean up_info) {
            this.up_info = up_info;
        }

        public UserStatusBean getUser_status() {
            return user_status;
        }

        public void setUser_status(UserStatusBean user_status) {
            this.user_status = user_status;
        }

        public List<AreasBean> getAreas() {
            return areas;
        }

        public void setAreas(List<AreasBean> areas) {
            this.areas = areas;
        }

        public List<EpisodesBean> getEpisodes() {
            return episodes;
        }

        public void setEpisodes(List<EpisodesBean> episodes) {
            this.episodes = episodes;
        }


        public static class ActivityBean implements Serializable{
            /**
             * head_bg_url :
             * id : 1634
             * title : 三体播放页会员购装扮中插
             */

            private String head_bg_url;
            private int id;
            private String title;

            public String getHead_bg_url() {
                return head_bg_url;
            }

            public void setHead_bg_url(String head_bg_url) {
                this.head_bg_url = head_bg_url;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        public static class FreyaBean implements Serializable{
            /**
             * bubble_desc : 你收到1个打招呼
             * bubble_show_cnt : 10000
             * icon_show : 1
             */

            private String bubble_desc;
            private int bubble_show_cnt;
            private int icon_show;

            public String getBubble_desc() {
                return bubble_desc;
            }

            public void setBubble_desc(String bubble_desc) {
                this.bubble_desc = bubble_desc;
            }

            public int getBubble_show_cnt() {
                return bubble_show_cnt;
            }

            public void setBubble_show_cnt(int bubble_show_cnt) {
                this.bubble_show_cnt = bubble_show_cnt;
            }

            public int getIcon_show() {
                return icon_show;
            }

            public void setIcon_show(int icon_show) {
                this.icon_show = icon_show;
            }
        }

        public static class NewEpBean implements Serializable{
            /**
             * desc : 连载中, 每周六11点，第5集起会员专享
             * id : 704475
             * is_new : 1
             * title : 3
             */

            private String desc;
            private int id;
            private int is_new;
            private String title;

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getIs_new() {
                return is_new;
            }

            public void setIs_new(int is_new) {
                this.is_new = is_new;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        public static class PaymentBean implements Serializable{
            /**
             * discount : 100
             * pay_type : {"allow_discount":0,"allow_pack":0,"allow_ticket":0,"allow_time_limit":0,"allow_vip_discount":0,"forbid_bb":0}
             * price : 0.0
             * promotion :
             * tip : 大会员专享观看特权哦~
             * view_start_time : 0
             * vip_discount : 100
             * vip_first_promotion :
             * vip_promotion : 成为大会员抢先看
             */

            private int discount;
            private PayTypeBean pay_type;
            private String price;
            private String promotion;
            private String tip;
            private int view_start_time;
            private int vip_discount;
            private String vip_first_promotion;
            private String vip_promotion;

            public int getDiscount() {
                return discount;
            }

            public void setDiscount(int discount) {
                this.discount = discount;
            }

            public PayTypeBean getPay_type() {
                return pay_type;
            }

            public void setPay_type(PayTypeBean pay_type) {
                this.pay_type = pay_type;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getPromotion() {
                return promotion;
            }

            public void setPromotion(String promotion) {
                this.promotion = promotion;
            }

            public String getTip() {
                return tip;
            }

            public void setTip(String tip) {
                this.tip = tip;
            }

            public int getView_start_time() {
                return view_start_time;
            }

            public void setView_start_time(int view_start_time) {
                this.view_start_time = view_start_time;
            }

            public int getVip_discount() {
                return vip_discount;
            }

            public void setVip_discount(int vip_discount) {
                this.vip_discount = vip_discount;
            }

            public String getVip_first_promotion() {
                return vip_first_promotion;
            }

            public void setVip_first_promotion(String vip_first_promotion) {
                this.vip_first_promotion = vip_first_promotion;
            }

            public String getVip_promotion() {
                return vip_promotion;
            }

            public void setVip_promotion(String vip_promotion) {
                this.vip_promotion = vip_promotion;
            }

            public static class PayTypeBean implements Serializable{
                /**
                 * allow_discount : 0
                 * allow_pack : 0
                 * allow_ticket : 0
                 * allow_time_limit : 0
                 * allow_vip_discount : 0
                 * forbid_bb : 0
                 */

                private int allow_discount;
                private int allow_pack;
                private int allow_ticket;
                private int allow_time_limit;
                private int allow_vip_discount;
                private int forbid_bb;

                public int getAllow_discount() {
                    return allow_discount;
                }

                public void setAllow_discount(int allow_discount) {
                    this.allow_discount = allow_discount;
                }

                public int getAllow_pack() {
                    return allow_pack;
                }

                public void setAllow_pack(int allow_pack) {
                    this.allow_pack = allow_pack;
                }

                public int getAllow_ticket() {
                    return allow_ticket;
                }

                public void setAllow_ticket(int allow_ticket) {
                    this.allow_ticket = allow_ticket;
                }

                public int getAllow_time_limit() {
                    return allow_time_limit;
                }

                public void setAllow_time_limit(int allow_time_limit) {
                    this.allow_time_limit = allow_time_limit;
                }

                public int getAllow_vip_discount() {
                    return allow_vip_discount;
                }

                public void setAllow_vip_discount(int allow_vip_discount) {
                    this.allow_vip_discount = allow_vip_discount;
                }

                public int getForbid_bb() {
                    return forbid_bb;
                }

                public void setForbid_bb(int forbid_bb) {
                    this.forbid_bb = forbid_bb;
                }
            }
        }

        public static class PositiveBean implements Serializable{
            /**
             * id : 35659
             * title : 正片
             */

            private int id;
            private String title;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        public static class PublishBean implements Serializable{
            /**
             * is_finish : 0
             * is_started : 1
             * pub_time : 2022-12-10 11:00:00
             * pub_time_show : 12月10日11:00
             * unknow_pub_date : 0
             * weekday : 0
             */

            private int is_finish;
            private int is_started;
            private String pub_time;
            private String pub_time_show;
            private int unknow_pub_date;
            private int weekday;

            public int getIs_finish() {
                return is_finish;
            }

            public void setIs_finish(int is_finish) {
                this.is_finish = is_finish;
            }

            public int getIs_started() {
                return is_started;
            }

            public void setIs_started(int is_started) {
                this.is_started = is_started;
            }

            public String getPub_time() {
                return pub_time;
            }

            public void setPub_time(String pub_time) {
                this.pub_time = pub_time;
            }

            public String getPub_time_show() {
                return pub_time_show;
            }

            public void setPub_time_show(String pub_time_show) {
                this.pub_time_show = pub_time_show;
            }

            public int getUnknow_pub_date() {
                return unknow_pub_date;
            }

            public void setUnknow_pub_date(int unknow_pub_date) {
                this.unknow_pub_date = unknow_pub_date;
            }

            public int getWeekday() {
                return weekday;
            }

            public void setWeekday(int weekday) {
                this.weekday = weekday;
            }
        }

        public static class RatingBean implements Serializable{
            /**
             * count : 7575
             * score : 8.2
             */

            private int count;
            private double score;

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public double getScore() {
                return score;
            }

            public void setScore(double score) {
                this.score = score;
            }
        }

        public static class RightsBean implements Serializable{
            /**
             * allow_bp : 0
             * allow_bp_rank : 0
             * allow_download : 0
             * allow_review : 1
             * area_limit : 340
             * ban_area_show : 1
             * can_watch : 1
             * copyright : dujia
             * forbid_pre : 0
             * freya_white : 0
             * is_cover_show : 0
             * is_preview : 1
             * only_vip_download : 0
             * resource :
             * watch_platform : 0
             */

            private int allow_bp;
            private int allow_bp_rank;
            private int allow_download;
            private int allow_review;
            private int area_limit;
            private int ban_area_show;
            private int can_watch;
            private String copyright;
            private int forbid_pre;
            private int freya_white;
            private int is_cover_show;
            private int is_preview;
            private int only_vip_download;
            private String resource;
            private int watch_platform;

            public int getAllow_bp() {
                return allow_bp;
            }

            public void setAllow_bp(int allow_bp) {
                this.allow_bp = allow_bp;
            }

            public int getAllow_bp_rank() {
                return allow_bp_rank;
            }

            public void setAllow_bp_rank(int allow_bp_rank) {
                this.allow_bp_rank = allow_bp_rank;
            }

            public int getAllow_download() {
                return allow_download;
            }

            public void setAllow_download(int allow_download) {
                this.allow_download = allow_download;
            }

            public int getAllow_review() {
                return allow_review;
            }

            public void setAllow_review(int allow_review) {
                this.allow_review = allow_review;
            }

            public int getArea_limit() {
                return area_limit;
            }

            public void setArea_limit(int area_limit) {
                this.area_limit = area_limit;
            }

            public int getBan_area_show() {
                return ban_area_show;
            }

            public void setBan_area_show(int ban_area_show) {
                this.ban_area_show = ban_area_show;
            }

            public int getCan_watch() {
                return can_watch;
            }

            public void setCan_watch(int can_watch) {
                this.can_watch = can_watch;
            }

            public String getCopyright() {
                return copyright;
            }

            public void setCopyright(String copyright) {
                this.copyright = copyright;
            }

            public int getForbid_pre() {
                return forbid_pre;
            }

            public void setForbid_pre(int forbid_pre) {
                this.forbid_pre = forbid_pre;
            }

            public int getFreya_white() {
                return freya_white;
            }

            public void setFreya_white(int freya_white) {
                this.freya_white = freya_white;
            }

            public int getIs_cover_show() {
                return is_cover_show;
            }

            public void setIs_cover_show(int is_cover_show) {
                this.is_cover_show = is_cover_show;
            }

            public int getIs_preview() {
                return is_preview;
            }

            public void setIs_preview(int is_preview) {
                this.is_preview = is_preview;
            }

            public int getOnly_vip_download() {
                return only_vip_download;
            }

            public void setOnly_vip_download(int only_vip_download) {
                this.only_vip_download = only_vip_download;
            }

            public String getResource() {
                return resource;
            }

            public void setResource(String resource) {
                this.resource = resource;
            }

            public int getWatch_platform() {
                return watch_platform;
            }

            public void setWatch_platform(int watch_platform) {
                this.watch_platform = watch_platform;
            }
        }

        public static class SeriesBean implements Serializable{
            /**
             * display_type : 0
             * series_id : 4260
             * series_title : 三体
             */

            private int display_type;
            private int series_id;
            private String series_title;

            public int getDisplay_type() {
                return display_type;
            }

            public void setDisplay_type(int display_type) {
                this.display_type = display_type;
            }

            public int getSeries_id() {
                return series_id;
            }

            public void setSeries_id(int series_id) {
                this.series_id = series_id;
            }

            public String getSeries_title() {
                return series_title;
            }

            public void setSeries_title(String series_title) {
                this.series_title = series_title;
            }
        }

        public static class ShowBean implements Serializable{
            /**
             * wide_screen : 0
             */

            private int wide_screen;

            public int getWide_screen() {
                return wide_screen;
            }

            public void setWide_screen(int wide_screen) {
                this.wide_screen = wide_screen;
            }
        }

        public static class StatBean implements Serializable{
            /**
             * coins : 1642423
             * danmakus : 330118
             * favorite : 406697
             * favorites : 6512751
             * likes : 2835320
             * reply : 362891
             * share : 1129706
             * views : 161123948
             */

            private int coins;
            private int danmakus;
            private int favorite;
            private int favorites;
            private int likes;
            private int reply;
            private int share;
            private int views;

            public int getCoins() {
                return coins;
            }

            public void setCoins(int coins) {
                this.coins = coins;
            }

            public int getDanmakus() {
                return danmakus;
            }

            public void setDanmakus(int danmakus) {
                this.danmakus = danmakus;
            }

            public int getFavorite() {
                return favorite;
            }

            public void setFavorite(int favorite) {
                this.favorite = favorite;
            }

            public int getFavorites() {
                return favorites;
            }

            public void setFavorites(int favorites) {
                this.favorites = favorites;
            }

            public int getLikes() {
                return likes;
            }

            public void setLikes(int likes) {
                this.likes = likes;
            }

            public int getReply() {
                return reply;
            }

            public void setReply(int reply) {
                this.reply = reply;
            }

            public int getShare() {
                return share;
            }

            public void setShare(int share) {
                this.share = share;
            }

            public int getViews() {
                return views;
            }

            public void setViews(int views) {
                this.views = views;
            }
        }

        public static class UpInfoBean implements Serializable{
            /**
             * avatar : https://i2.hdslb.com/bfs/face/38fbf0a10f7fb0e8910be0e662b05bcb0aed830c.jpg
             * avatar_subscript_url :
             * follower : 4583274
             * is_follow : 0
             * mid : 98627270
             * nickname_color : #FB7299
             * pendant : {"image":"https://i2.hdslb.com/bfs/garb/item/4ab1a5a6e07a99e649cde625c06eeb1c15585156.png","name":"罗小黑战记","pid":5108}
             * theme_type : 0
             * uname : 哔哩哔哩国创
             * verify_type : 3
             * vip_label : {"bg_color":"#FB7299","bg_style":1,"border_color":"","text":"十年大会员","text_color":"#FFFFFF"}
             * vip_status : 1
             * vip_type : 2
             */

            private String avatar;
            private String avatar_subscript_url;
            private int follower;
            private int is_follow;
            private long mid;
            private String nickname_color;
            private PendantBean pendant;
            private int theme_type;
            private String uname;
            private int verify_type;
            private VipLabelBean vip_label;
            private int vip_status;
            private int vip_type;

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getAvatar_subscript_url() {
                return avatar_subscript_url;
            }

            public void setAvatar_subscript_url(String avatar_subscript_url) {
                this.avatar_subscript_url = avatar_subscript_url;
            }

            public int getFollower() {
                return follower;
            }

            public void setFollower(int follower) {
                this.follower = follower;
            }

            public int getIs_follow() {
                return is_follow;
            }

            public void setIs_follow(int is_follow) {
                this.is_follow = is_follow;
            }

            public long getMid() {
                return mid;
            }

            public void setMid(long mid) {
                this.mid = mid;
            }

            public String getNickname_color() {
                return nickname_color;
            }

            public void setNickname_color(String nickname_color) {
                this.nickname_color = nickname_color;
            }

            public PendantBean getPendant() {
                return pendant;
            }

            public void setPendant(PendantBean pendant) {
                this.pendant = pendant;
            }

            public int getTheme_type() {
                return theme_type;
            }

            public void setTheme_type(int theme_type) {
                this.theme_type = theme_type;
            }

            public String getUname() {
                return uname;
            }

            public void setUname(String uname) {
                this.uname = uname;
            }

            public int getVerify_type() {
                return verify_type;
            }

            public void setVerify_type(int verify_type) {
                this.verify_type = verify_type;
            }

            public VipLabelBean getVip_label() {
                return vip_label;
            }

            public void setVip_label(VipLabelBean vip_label) {
                this.vip_label = vip_label;
            }

            public int getVip_status() {
                return vip_status;
            }

            public void setVip_status(int vip_status) {
                this.vip_status = vip_status;
            }

            public int getVip_type() {
                return vip_type;
            }

            public void setVip_type(int vip_type) {
                this.vip_type = vip_type;
            }

            public static class PendantBean implements Serializable{
                /**
                 * image : https://i2.hdslb.com/bfs/garb/item/4ab1a5a6e07a99e649cde625c06eeb1c15585156.png
                 * name : 罗小黑战记
                 * pid : 5108
                 */

                private String image;
                private String name;
                private int pid;

                public String getImage() {
                    return image;
                }

                public void setImage(String image) {
                    this.image = image;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getPid() {
                    return pid;
                }

                public void setPid(int pid) {
                    this.pid = pid;
                }
            }

            public static class VipLabelBean implements Serializable{
                /**
                 * bg_color : #FB7299
                 * bg_style : 1
                 * border_color :
                 * text : 十年大会员
                 * text_color : #FFFFFF
                 */

                private String bg_color;
                private int bg_style;
                private String border_color;
                private String text;
                private String text_color;

                public String getBg_color() {
                    return bg_color;
                }

                public void setBg_color(String bg_color) {
                    this.bg_color = bg_color;
                }

                public int getBg_style() {
                    return bg_style;
                }

                public void setBg_style(int bg_style) {
                    this.bg_style = bg_style;
                }

                public String getBorder_color() {
                    return border_color;
                }

                public void setBorder_color(String border_color) {
                    this.border_color = border_color;
                }

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public String getText_color() {
                    return text_color;
                }

                public void setText_color(String text_color) {
                    this.text_color = text_color;
                }
            }
        }

        public static class UserStatusBean implements Serializable{
            /**
             * area_limit : 0
             * ban_area_show : 0
             * follow : 0
             * follow_status : 0
             * login : 0
             * pay : 0
             * pay_pack_paid : 0
             * sponsor : 0
             */

            private int area_limit;
            private int ban_area_show;
            private int follow;
            private int follow_status;
            private int login;
            private int pay;
            private int pay_pack_paid;
            private int sponsor;

            public int getArea_limit() {
                return area_limit;
            }

            public void setArea_limit(int area_limit) {
                this.area_limit = area_limit;
            }

            public int getBan_area_show() {
                return ban_area_show;
            }

            public void setBan_area_show(int ban_area_show) {
                this.ban_area_show = ban_area_show;
            }

            public int getFollow() {
                return follow;
            }

            public void setFollow(int follow) {
                this.follow = follow;
            }

            public int getFollow_status() {
                return follow_status;
            }

            public void setFollow_status(int follow_status) {
                this.follow_status = follow_status;
            }

            public int getLogin() {
                return login;
            }

            public void setLogin(int login) {
                this.login = login;
            }

            public int getPay() {
                return pay;
            }

            public void setPay(int pay) {
                this.pay = pay;
            }

            public int getPay_pack_paid() {
                return pay_pack_paid;
            }

            public void setPay_pack_paid(int pay_pack_paid) {
                this.pay_pack_paid = pay_pack_paid;
            }

            public int getSponsor() {
                return sponsor;
            }

            public void setSponsor(int sponsor) {
                this.sponsor = sponsor;
            }
        }

        public static class AreasBean implements Serializable{
            /**
             * id : 1
             * name : 中国大陆
             */

            private int id;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class EpisodesBean implements Serializable{
            /**
             * aid : 476022007
             * badge :
             * badge_info : {"bg_color":"#FB7299","bg_color_night":"#BB5B76","text":""}
             * badge_type : 0
             * bvid : BV1iK41197nZ
             * cid : 927119874
             * cover : http://i0.hdslb.com/bfs/archive/9163ca2abcd22706bf055a04bcb4018e335fe253.jpg
             * dimension : {"height":2160,"rotate":0,"width":3840}
             * duration : 2244720
             * from : bangumi
             * id : 704473
             * is_view_hide : false
             * link : https://www.bilibili.com/bangumi/play/ep704473
             * long_title : 意义之塔
             * pub_time : 1670641200
             * pv : 0
             * release_date :
             * rights : {"allow_demand":0,"allow_dm":1,"allow_download":1,"area_limit":0}
             * share_copy : 《三体》第1话 意义之塔
             * share_url : https://www.bilibili.com/bangumi/play/ep704473
             * short_link : https://b23.tv/ep704473
             * status : 2
             * subtitle : 已观看1.6亿次
             * title : 1
             * vid :
             */

            private int checkState = 0;
            private int selected = 0;
            private int aid;
            private String badge;
            private BadgeInfoBean badge_info;
            private int badge_type;
            private String bvid;
            private int cid;
            private String cover;
            private DimensionBean dimension;
            private int duration;
            private String from;
            private long id;
            private boolean is_view_hide;
            private String link;
            private String long_title;
            private int pub_time;
            private int pv;
            private String release_date;
            private RightsBeanX rights;
            private String share_copy;
            private String share_url;
            private String short_link;
            private int status;
            private String subtitle;
            private String title;
            private String vid;

            public int getCheckState() {
                return checkState;
            }

            public void setCheckState(int checkState) {
                this.checkState = checkState;
            }

            public int getSelected() {
                return selected;
            }

            public void setSelected(int selected) {
                this.selected = selected;
            }

            public int getAid() {
                return aid;
            }

            public void setAid(int aid) {
                this.aid = aid;
            }

            public String getBadge() {
                return badge;
            }

            public void setBadge(String badge) {
                this.badge = badge;
            }

            public BadgeInfoBean getBadge_info() {
                return badge_info;
            }

            public void setBadge_info(BadgeInfoBean badge_info) {
                this.badge_info = badge_info;
            }

            public int getBadge_type() {
                return badge_type;
            }

            public void setBadge_type(int badge_type) {
                this.badge_type = badge_type;
            }

            public String getBvid() {
                return bvid;
            }

            public void setBvid(String bvid) {
                this.bvid = bvid;
            }

            public int getCid() {
                return cid;
            }

            public void setCid(int cid) {
                this.cid = cid;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public DimensionBean getDimension() {
                return dimension;
            }

            public void setDimension(DimensionBean dimension) {
                this.dimension = dimension;
            }

            public int getDuration() {
                return duration;
            }

            public void setDuration(int duration) {
                this.duration = duration;
            }

            public String getFrom() {
                return from;
            }

            public void setFrom(String from) {
                this.from = from;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public boolean isIs_view_hide() {
                return is_view_hide;
            }

            public void setIs_view_hide(boolean is_view_hide) {
                this.is_view_hide = is_view_hide;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getLong_title() {
                return long_title;
            }

            public void setLong_title(String long_title) {
                this.long_title = long_title;
            }

            public int getPub_time() {
                return pub_time;
            }

            public void setPub_time(int pub_time) {
                this.pub_time = pub_time;
            }

            public int getPv() {
                return pv;
            }

            public void setPv(int pv) {
                this.pv = pv;
            }

            public String getRelease_date() {
                return release_date;
            }

            public void setRelease_date(String release_date) {
                this.release_date = release_date;
            }

            public RightsBeanX getRights() {
                return rights;
            }

            public void setRights(RightsBeanX rights) {
                this.rights = rights;
            }

            public String getShare_copy() {
                return share_copy;
            }

            public void setShare_copy(String share_copy) {
                this.share_copy = share_copy;
            }

            public String getShare_url() {
                return share_url;
            }

            public void setShare_url(String share_url) {
                this.share_url = share_url;
            }

            public String getShort_link() {
                return short_link;
            }

            public void setShort_link(String short_link) {
                this.short_link = short_link;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getSubtitle() {
                return subtitle;
            }

            public void setSubtitle(String subtitle) {
                this.subtitle = subtitle;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getVid() {
                return vid;
            }

            public void setVid(String vid) {
                this.vid = vid;
            }

            public static class BadgeInfoBean implements Serializable{
                /**
                 * bg_color : #FB7299
                 * bg_color_night : #BB5B76
                 * text :
                 */

                private String bg_color;
                private String bg_color_night;
                private String text;

                public String getBg_color() {
                    return bg_color;
                }

                public void setBg_color(String bg_color) {
                    this.bg_color = bg_color;
                }

                public String getBg_color_night() {
                    return bg_color_night;
                }

                public void setBg_color_night(String bg_color_night) {
                    this.bg_color_night = bg_color_night;
                }

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }
            }

            public static class DimensionBean implements Serializable{
                /**
                 * height : 2160
                 * rotate : 0
                 * width : 3840
                 */

                private int height;
                private int rotate;
                private int width;

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

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }
            }

            public static class RightsBeanX implements Serializable{
                /**
                 * allow_demand : 0
                 * allow_dm : 1
                 * allow_download : 1
                 * area_limit : 0
                 */

                private int allow_demand;
                private int allow_dm;
                private int allow_download;
                private int area_limit;

                public int getAllow_demand() {
                    return allow_demand;
                }

                public void setAllow_demand(int allow_demand) {
                    this.allow_demand = allow_demand;
                }

                public int getAllow_dm() {
                    return allow_dm;
                }

                public void setAllow_dm(int allow_dm) {
                    this.allow_dm = allow_dm;
                }

                public int getAllow_download() {
                    return allow_download;
                }

                public void setAllow_download(int allow_download) {
                    this.allow_download = allow_download;
                }

                public int getArea_limit() {
                    return area_limit;
                }

                public void setArea_limit(int area_limit) {
                    this.area_limit = area_limit;
                }
            }
        }

        public static class SeasonsBean implements Serializable{
            /**
             * badge : 出品
             * badge_info : {"bg_color":"#00C0FF","bg_color_night":"#0B91BE","text":"出品"}
             * badge_type : 1
             * cover : http://i0.hdslb.com/bfs/bangumi/image/9870f898b8a39bbb8048f34317f8d78a02cc1770.png
             * horizontal_cover_1610 : http://i0.hdslb.com/bfs/bangumi/image/d236dc9c7aab08d77182a5ff3b2dc1aaf5c76217.png
             * horizontal_cover_169 : http://i0.hdslb.com/bfs/bangumi/image/6f12d97edbad9cf109b0bba250e6c84f13b30bdb.png
             * media_id : 4315402
             * new_ep : {"cover":"http://i0.hdslb.com/bfs/archive/487cbe9ca8f35dc043a907c5c81097c1ec3da7db.jpg","id":704475,"index_show":"更新至第3话"}
             * season_id : 26257
             * season_title : 第一季
             * season_type : 4
             * stat : {"favorites":6512751,"series_follow":6521332,"views":161123948}
             */

            private String badge;
            private BadgeInfoBeanX badge_info;
            private int badge_type;
            private String cover;
            private String horizontal_cover_1610;
            private String horizontal_cover_169;
            private int media_id;
            private NewEpBeanX new_ep;
            private int season_id;
            private String season_title;
            private int season_type;
            private StatBeanX stat;

            public String getBadge() {
                return badge;
            }

            public void setBadge(String badge) {
                this.badge = badge;
            }

            public BadgeInfoBeanX getBadge_info() {
                return badge_info;
            }

            public void setBadge_info(BadgeInfoBeanX badge_info) {
                this.badge_info = badge_info;
            }

            public int getBadge_type() {
                return badge_type;
            }

            public void setBadge_type(int badge_type) {
                this.badge_type = badge_type;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getHorizontal_cover_1610() {
                return horizontal_cover_1610;
            }

            public void setHorizontal_cover_1610(String horizontal_cover_1610) {
                this.horizontal_cover_1610 = horizontal_cover_1610;
            }

            public String getHorizontal_cover_169() {
                return horizontal_cover_169;
            }

            public void setHorizontal_cover_169(String horizontal_cover_169) {
                this.horizontal_cover_169 = horizontal_cover_169;
            }

            public int getMedia_id() {
                return media_id;
            }

            public void setMedia_id(int media_id) {
                this.media_id = media_id;
            }

            public NewEpBeanX getNew_ep() {
                return new_ep;
            }

            public void setNew_ep(NewEpBeanX new_ep) {
                this.new_ep = new_ep;
            }

            public int getSeason_id() {
                return season_id;
            }

            public void setSeason_id(int season_id) {
                this.season_id = season_id;
            }

            public String getSeason_title() {
                return season_title;
            }

            public void setSeason_title(String season_title) {
                this.season_title = season_title;
            }

            public int getSeason_type() {
                return season_type;
            }

            public void setSeason_type(int season_type) {
                this.season_type = season_type;
            }

            public StatBeanX getStat() {
                return stat;
            }

            public void setStat(StatBeanX stat) {
                this.stat = stat;
            }

            public static class BadgeInfoBeanX implements Serializable{
                /**
                 * bg_color : #00C0FF
                 * bg_color_night : #0B91BE
                 * text : 出品
                 */

                private String bg_color;
                private String bg_color_night;
                private String text;

                public String getBg_color() {
                    return bg_color;
                }

                public void setBg_color(String bg_color) {
                    this.bg_color = bg_color;
                }

                public String getBg_color_night() {
                    return bg_color_night;
                }

                public void setBg_color_night(String bg_color_night) {
                    this.bg_color_night = bg_color_night;
                }

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }
            }

            public static class NewEpBeanX implements Serializable{
                /**
                 * cover : http://i0.hdslb.com/bfs/archive/487cbe9ca8f35dc043a907c5c81097c1ec3da7db.jpg
                 * id : 704475
                 * index_show : 更新至第3话
                 */

                private String cover;
                private int id;
                private String index_show;

                public String getCover() {
                    return cover;
                }

                public void setCover(String cover) {
                    this.cover = cover;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getIndex_show() {
                    return index_show;
                }

                public void setIndex_show(String index_show) {
                    this.index_show = index_show;
                }
            }

            public static class StatBeanX implements Serializable{
                /**
                 * favorites : 6512751
                 * series_follow : 6521332
                 * views : 161123948
                 */

                private int favorites;
                private int series_follow;
                private int views;

                public int getFavorites() {
                    return favorites;
                }

                public void setFavorites(int favorites) {
                    this.favorites = favorites;
                }

                public int getSeries_follow() {
                    return series_follow;
                }

                public void setSeries_follow(int series_follow) {
                    this.series_follow = series_follow;
                }

                public int getViews() {
                    return views;
                }

                public void setViews(int views) {
                    this.views = views;
                }
            }
        }

        public static class SectionBean implements Serializable{
            private int attr;
            private int episode_id;
            private int id;
            private String title;
            private int type;
            private List<?> episode_ids;
            private List<EpisodesBeanX> episodes;

            public int getAttr() {
                return attr;
            }

            public void setAttr(int attr) {
                this.attr = attr;
            }

            public int getEpisode_id() {
                return episode_id;
            }

            public void setEpisode_id(int episode_id) {
                this.episode_id = episode_id;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public List<?> getEpisode_ids() {
                return episode_ids;
            }

            public void setEpisode_ids(List<?> episode_ids) {
                this.episode_ids = episode_ids;
            }

            public List<EpisodesBeanX> getEpisodes() {
                return episodes;
            }

            public void setEpisodes(List<EpisodesBeanX> episodes) {
                this.episodes = episodes;
            }

            public static class EpisodesBeanX implements Serializable{
                /**
                 * aid : 648575736
                 * badge :
                 * badge_info : {"bg_color":"#FB7299","bg_color_night":"#BB5B76","text":""}
                 * badge_type : 0
                 * bvid : BV1pe4y1T7QU
                 * cid : 919377877
                 * cover : http://i0.hdslb.com/bfs/archive/db97e815b045169468d43283cd2b54ffbc8b3557.jpg
                 * dimension : {"height":1080,"rotate":0,"width":1920}
                 * duration : 116000
                 * from : bangumi
                 * id : 704787
                 * is_view_hide : false
                 * link : https://www.bilibili.com/bangumi/play/ep704787
                 * long_title : 纯享版
                 * pub_time : 1670727600
                 * pv : 0
                 * release_date :
                 * rights : {"allow_demand":0,"allow_dm":1,"allow_download":1,"area_limit":0}
                 * share_copy : 《三体》OP 纯享版
                 * share_url : https://www.bilibili.com/bangumi/play/ep704787
                 * short_link : https://b23.tv/ep704787
                 * stat : {"coin":7039,"danmakus":442,"likes":25510,"play":1513633,"reply":1566}
                 * status : 2
                 * subtitle : 已观看1.6亿次
                 * title : OP
                 * vid :
                 */

                private int aid;
                private String badge;
                private BadgeInfoBeanXX badge_info;
                private int badge_type;
                private String bvid;
                private int cid;
                private String cover;
                private DimensionBeanX dimension;
                private int duration;
                private String from;
                private int id;
                private boolean is_view_hide;
                private String link;
                private String long_title;
                private int pub_time;
                private int pv;
                private String release_date;
                private RightsBeanXX rights;
                private String share_copy;
                private String share_url;
                private String short_link;
                private StatBeanXX stat;
                private int status;
                private String subtitle;
                private String title;
                private String vid;

                public int getAid() {
                    return aid;
                }

                public void setAid(int aid) {
                    this.aid = aid;
                }

                public String getBadge() {
                    return badge;
                }

                public void setBadge(String badge) {
                    this.badge = badge;
                }

                public BadgeInfoBeanXX getBadge_info() {
                    return badge_info;
                }

                public void setBadge_info(BadgeInfoBeanXX badge_info) {
                    this.badge_info = badge_info;
                }

                public int getBadge_type() {
                    return badge_type;
                }

                public void setBadge_type(int badge_type) {
                    this.badge_type = badge_type;
                }

                public String getBvid() {
                    return bvid;
                }

                public void setBvid(String bvid) {
                    this.bvid = bvid;
                }

                public int getCid() {
                    return cid;
                }

                public void setCid(int cid) {
                    this.cid = cid;
                }

                public String getCover() {
                    return cover;
                }

                public void setCover(String cover) {
                    this.cover = cover;
                }

                public DimensionBeanX getDimension() {
                    return dimension;
                }

                public void setDimension(DimensionBeanX dimension) {
                    this.dimension = dimension;
                }

                public int getDuration() {
                    return duration;
                }

                public void setDuration(int duration) {
                    this.duration = duration;
                }

                public String getFrom() {
                    return from;
                }

                public void setFrom(String from) {
                    this.from = from;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public boolean isIs_view_hide() {
                    return is_view_hide;
                }

                public void setIs_view_hide(boolean is_view_hide) {
                    this.is_view_hide = is_view_hide;
                }

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }

                public String getLong_title() {
                    return long_title;
                }

                public void setLong_title(String long_title) {
                    this.long_title = long_title;
                }

                public int getPub_time() {
                    return pub_time;
                }

                public void setPub_time(int pub_time) {
                    this.pub_time = pub_time;
                }

                public int getPv() {
                    return pv;
                }

                public void setPv(int pv) {
                    this.pv = pv;
                }

                public String getRelease_date() {
                    return release_date;
                }

                public void setRelease_date(String release_date) {
                    this.release_date = release_date;
                }

                public RightsBeanXX getRights() {
                    return rights;
                }

                public void setRights(RightsBeanXX rights) {
                    this.rights = rights;
                }

                public String getShare_copy() {
                    return share_copy;
                }

                public void setShare_copy(String share_copy) {
                    this.share_copy = share_copy;
                }

                public String getShare_url() {
                    return share_url;
                }

                public void setShare_url(String share_url) {
                    this.share_url = share_url;
                }

                public String getShort_link() {
                    return short_link;
                }

                public void setShort_link(String short_link) {
                    this.short_link = short_link;
                }

                public StatBeanXX getStat() {
                    return stat;
                }

                public void setStat(StatBeanXX stat) {
                    this.stat = stat;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public String getSubtitle() {
                    return subtitle;
                }

                public void setSubtitle(String subtitle) {
                    this.subtitle = subtitle;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getVid() {
                    return vid;
                }

                public void setVid(String vid) {
                    this.vid = vid;
                }

                public static class BadgeInfoBeanXX implements Serializable{
                    /**
                     * bg_color : #FB7299
                     * bg_color_night : #BB5B76
                     * text :
                     */

                    private String bg_color;
                    private String bg_color_night;
                    private String text;

                    public String getBg_color() {
                        return bg_color;
                    }

                    public void setBg_color(String bg_color) {
                        this.bg_color = bg_color;
                    }

                    public String getBg_color_night() {
                        return bg_color_night;
                    }

                    public void setBg_color_night(String bg_color_night) {
                        this.bg_color_night = bg_color_night;
                    }

                    public String getText() {
                        return text;
                    }

                    public void setText(String text) {
                        this.text = text;
                    }
                }

                public static class DimensionBeanX implements Serializable{
                    /**
                     * height : 1080
                     * rotate : 0
                     * width : 1920
                     */

                    private int height;
                    private int rotate;
                    private int width;

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

                    public int getWidth() {
                        return width;
                    }

                    public void setWidth(int width) {
                        this.width = width;
                    }
                }

                public static class RightsBeanXX implements Serializable{
                    /**
                     * allow_demand : 0
                     * allow_dm : 1
                     * allow_download : 1
                     * area_limit : 0
                     */

                    private int allow_demand;
                    private int allow_dm;
                    private int allow_download;
                    private int area_limit;

                    public int getAllow_demand() {
                        return allow_demand;
                    }

                    public void setAllow_demand(int allow_demand) {
                        this.allow_demand = allow_demand;
                    }

                    public int getAllow_dm() {
                        return allow_dm;
                    }

                    public void setAllow_dm(int allow_dm) {
                        this.allow_dm = allow_dm;
                    }

                    public int getAllow_download() {
                        return allow_download;
                    }

                    public void setAllow_download(int allow_download) {
                        this.allow_download = allow_download;
                    }

                    public int getArea_limit() {
                        return area_limit;
                    }

                    public void setArea_limit(int area_limit) {
                        this.area_limit = area_limit;
                    }
                }

                public static class StatBeanXX implements Serializable{
                    /**
                     * coin : 7039
                     * danmakus : 442
                     * likes : 25510
                     * play : 1513633
                     * reply : 1566
                     */

                    private int coin;
                    private int danmakus;
                    private int likes;
                    private int play;
                    private int reply;

                    public int getCoin() {
                        return coin;
                    }

                    public void setCoin(int coin) {
                        this.coin = coin;
                    }

                    public int getDanmakus() {
                        return danmakus;
                    }

                    public void setDanmakus(int danmakus) {
                        this.danmakus = danmakus;
                    }

                    public int getLikes() {
                        return likes;
                    }

                    public void setLikes(int likes) {
                        this.likes = likes;
                    }

                    public int getPlay() {
                        return play;
                    }

                    public void setPlay(int play) {
                        this.play = play;
                    }

                    public int getReply() {
                        return reply;
                    }

                    public void setReply(int reply) {
                        this.reply = reply;
                    }
                }
            }
        }
    }
}
