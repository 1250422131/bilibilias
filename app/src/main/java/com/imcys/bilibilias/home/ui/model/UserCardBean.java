package com.imcys.bilibilias.home.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author:imcys
 * @create: 2022-11-15 16:19
 * @Description: B站用户卡片信息类
 */
public class UserCardBean implements Serializable {


    /**
     * code : 0
     * message : 0
     * ttl : 1
     * data : {"card":{"mid":"488009671","name":"千雅折一湖Official","approve":false,"sex":"女","rank":"10000","face":"https://i1.hdslb.com/bfs/face/80718d76952753c6770b8497fd2b9b95fe9061cd.jpg","face_nft":0,"face_nft_type":0,"DisplayRank":"0","regtime":0,"spacesta":0,"birthday":"","place":"","description":"","article":0,"attentions":[],"fans":1286881,"friend":13,"attention":13,"sign":"在日本出生的中国籍宝宝????全家都是中国人\n????：baby133102","level_info":{"current_level":6,"current_min":0,"current_exp":0,"next_exp":0},"pendant":{"pid":0,"name":"","image":"","expire":0,"image_enhance":"","image_enhance_frame":""},"nameplate":{"nid":1,"name":"黄金殿堂","image":"https://i0.hdslb.com/bfs/face/82896ff40fcb4e7c7259cb98056975830cb55695.png","image_small":"https://i1.hdslb.com/bfs/face/627e342851dfda6fe7380c2fa0cbd7fae2e61533.png","level":"稀有勋章","condition":"单个自制视频总播放数>=100万"},"Official":{"role":2,"title":"千雅折一湖官方账号，vlog UP主","desc":"","type":0},"official_verify":{"type":0,"desc":"千雅折一湖官方账号，vlog UP主"},"vip":{"type":2,"status":1,"due_date":1698681600000,"vip_pay_type":0,"theme_type":0,"label":{"path":"","text":"年度大会员","label_theme":"annual_vip","text_color":"#FFFFFF","bg_style":1,"bg_color":"#FB7299","border_color":"","use_img_label":true,"img_label_uri_hans":"","img_label_uri_hant":"","img_label_uri_hans_static":"https://i0.hdslb.com/bfs/vip/8d4f8bfc713826a5412a0a27eaaac4d6b9ede1d9.png","img_label_uri_hant_static":"https://i0.hdslb.com/bfs/activity-plat/static/20220614/e369244d0b14644f5e1a06431e22a4d5/VEW8fCC0hg.png"},"avatar_subscript":1,"nickname_color":"#FB7299","role":3,"avatar_subscript_url":"","tv_vip_status":0,"tv_vip_pay_type":0,"vipType":2,"vipStatus":1},"is_senior_member":0},"following":false,"archive_count":598,"article_count":0,"follower":1286881,"like_num":13201548}
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
         * card : {"mid":"488009671","name":"千雅折一湖Official","approve":false,"sex":"女","rank":"10000","face":"https://i1.hdslb.com/bfs/face/80718d76952753c6770b8497fd2b9b95fe9061cd.jpg","face_nft":0,"face_nft_type":0,"DisplayRank":"0","regtime":0,"spacesta":0,"birthday":"","place":"","description":"","article":0,"attentions":[],"fans":1286881,"friend":13,"attention":13,"sign":"在日本出生的中国籍宝宝????全家都是中国人\n????：baby133102","level_info":{"current_level":6,"current_min":0,"current_exp":0,"next_exp":0},"pendant":{"pid":0,"name":"","image":"","expire":0,"image_enhance":"","image_enhance_frame":""},"nameplate":{"nid":1,"name":"黄金殿堂","image":"https://i0.hdslb.com/bfs/face/82896ff40fcb4e7c7259cb98056975830cb55695.png","image_small":"https://i1.hdslb.com/bfs/face/627e342851dfda6fe7380c2fa0cbd7fae2e61533.png","level":"稀有勋章","condition":"单个自制视频总播放数>=100万"},"Official":{"role":2,"title":"千雅折一湖官方账号，vlog UP主","desc":"","type":0},"official_verify":{"type":0,"desc":"千雅折一湖官方账号，vlog UP主"},"vip":{"type":2,"status":1,"due_date":1698681600000,"vip_pay_type":0,"theme_type":0,"label":{"path":"","text":"年度大会员","label_theme":"annual_vip","text_color":"#FFFFFF","bg_style":1,"bg_color":"#FB7299","border_color":"","use_img_label":true,"img_label_uri_hans":"","img_label_uri_hant":"","img_label_uri_hans_static":"https://i0.hdslb.com/bfs/vip/8d4f8bfc713826a5412a0a27eaaac4d6b9ede1d9.png","img_label_uri_hant_static":"https://i0.hdslb.com/bfs/activity-plat/static/20220614/e369244d0b14644f5e1a06431e22a4d5/VEW8fCC0hg.png"},"avatar_subscript":1,"nickname_color":"#FB7299","role":3,"avatar_subscript_url":"","tv_vip_status":0,"tv_vip_pay_type":0,"vipType":2,"vipStatus":1},"is_senior_member":0}
         * following : false
         * archive_count : 598
         * article_count : 0
         * follower : 1286881
         * like_num : 13201548
         */

        private CardBean card;
        private boolean following;
        private int archive_count;
        private int article_count;
        private int follower;
        private int like_num;

        public CardBean getCard() {
            return card;
        }

        public void setCard(CardBean card) {
            this.card = card;
        }

        public boolean isFollowing() {
            return following;
        }

        public void setFollowing(boolean following) {
            this.following = following;
        }

        public int getArchive_count() {
            return archive_count;
        }

        public void setArchive_count(int archive_count) {
            this.archive_count = archive_count;
        }

        public int getArticle_count() {
            return article_count;
        }

        public void setArticle_count(int article_count) {
            this.article_count = article_count;
        }

        public int getFollower() {
            return follower;
        }

        public void setFollower(int follower) {
            this.follower = follower;
        }

        public int getLike_num() {
            return like_num;
        }

        public void setLike_num(int like_num) {
            this.like_num = like_num;
        }

        public static class CardBean implements Serializable {
            /**
             * mid : 488009671
             * name : 千雅折一湖Official
             * approve : false
             * sex : 女
             * rank : 10000
             * face : https://i1.hdslb.com/bfs/face/80718d76952753c6770b8497fd2b9b95fe9061cd.jpg
             * face_nft : 0
             * face_nft_type : 0
             * DisplayRank : 0
             * regtime : 0
             * spacesta : 0
             * birthday :
             * place :
             * description :
             * article : 0
             * attentions : []
             * fans : 1286881
             * friend : 13
             * attention : 13
             * sign : 在日本出生的中国籍宝宝????全家都是中国人
             * ????：baby133102
             * level_info : {"current_level":6,"current_min":0,"current_exp":0,"next_exp":0}
             * pendant : {"pid":0,"name":"","image":"","expire":0,"image_enhance":"","image_enhance_frame":""}
             * nameplate : {"nid":1,"name":"黄金殿堂","image":"https://i0.hdslb.com/bfs/face/82896ff40fcb4e7c7259cb98056975830cb55695.png","image_small":"https://i1.hdslb.com/bfs/face/627e342851dfda6fe7380c2fa0cbd7fae2e61533.png","level":"稀有勋章","condition":"单个自制视频总播放数>=100万"}
             * Official : {"role":2,"title":"千雅折一湖官方账号，vlog UP主","desc":"","type":0}
             * official_verify : {"type":0,"desc":"千雅折一湖官方账号，vlog UP主"}
             * vip : {"type":2,"status":1,"due_date":1698681600000,"vip_pay_type":0,"theme_type":0,"label":{"path":"","text":"年度大会员","label_theme":"annual_vip","text_color":"#FFFFFF","bg_style":1,"bg_color":"#FB7299","border_color":"","use_img_label":true,"img_label_uri_hans":"","img_label_uri_hant":"","img_label_uri_hans_static":"https://i0.hdslb.com/bfs/vip/8d4f8bfc713826a5412a0a27eaaac4d6b9ede1d9.png","img_label_uri_hant_static":"https://i0.hdslb.com/bfs/activity-plat/static/20220614/e369244d0b14644f5e1a06431e22a4d5/VEW8fCC0hg.png"},"avatar_subscript":1,"nickname_color":"#FB7299","role":3,"avatar_subscript_url":"","tv_vip_status":0,"tv_vip_pay_type":0,"vipType":2,"vipStatus":1}
             * is_senior_member : 0
             */

            private String mid;
            private String name;
            private boolean approve;
            private String sex;
            private String rank;
            private String face;
            private int face_nft;
            private int face_nft_type;
            private String DisplayRank;
            private int regtime;
            private int spacesta;
            private String birthday;
            private String place;
            private String description;
            private int article;
            private int fans;
            private int friend;
            private int attention;
            private String sign;
            private LevelInfoBean level_info;
            private PendantBean pendant;
            private NameplateBean nameplate;
            private OfficialBean Official;
            private OfficialVerifyBean official_verify;
            private VipBean vip;
            private int is_senior_member;
            private List<?> attentions;

            public String getMid() {
                return mid;
            }

            public void setMid(String mid) {
                this.mid = mid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public boolean isApprove() {
                return approve;
            }

            public void setApprove(boolean approve) {
                this.approve = approve;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getRank() {
                return rank;
            }

            public void setRank(String rank) {
                this.rank = rank;
            }

            public String getFace() {
                return face;
            }

            public void setFace(String face) {
                this.face = face;
            }

            public int getFace_nft() {
                return face_nft;
            }

            public void setFace_nft(int face_nft) {
                this.face_nft = face_nft;
            }

            public int getFace_nft_type() {
                return face_nft_type;
            }

            public void setFace_nft_type(int face_nft_type) {
                this.face_nft_type = face_nft_type;
            }

            public String getDisplayRank() {
                return DisplayRank;
            }

            public void setDisplayRank(String DisplayRank) {
                this.DisplayRank = DisplayRank;
            }

            public int getRegtime() {
                return regtime;
            }

            public void setRegtime(int regtime) {
                this.regtime = regtime;
            }

            public int getSpacesta() {
                return spacesta;
            }

            public void setSpacesta(int spacesta) {
                this.spacesta = spacesta;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public String getPlace() {
                return place;
            }

            public void setPlace(String place) {
                this.place = place;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getArticle() {
                return article;
            }

            public void setArticle(int article) {
                this.article = article;
            }

            public int getFans() {
                return fans;
            }

            public void setFans(int fans) {
                this.fans = fans;
            }

            public int getFriend() {
                return friend;
            }

            public void setFriend(int friend) {
                this.friend = friend;
            }

            public int getAttention() {
                return attention;
            }

            public void setAttention(int attention) {
                this.attention = attention;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            public LevelInfoBean getLevel_info() {
                return level_info;
            }

            public void setLevel_info(LevelInfoBean level_info) {
                this.level_info = level_info;
            }

            public PendantBean getPendant() {
                return pendant;
            }

            public void setPendant(PendantBean pendant) {
                this.pendant = pendant;
            }

            public NameplateBean getNameplate() {
                return nameplate;
            }

            public void setNameplate(NameplateBean nameplate) {
                this.nameplate = nameplate;
            }

            public OfficialBean getOfficial() {
                return Official;
            }

            public void setOfficial(OfficialBean Official) {
                this.Official = Official;
            }

            public OfficialVerifyBean getOfficial_verify() {
                return official_verify;
            }

            public void setOfficial_verify(OfficialVerifyBean official_verify) {
                this.official_verify = official_verify;
            }

            public VipBean getVip() {
                return vip;
            }

            public void setVip(VipBean vip) {
                this.vip = vip;
            }

            public int getIs_senior_member() {
                return is_senior_member;
            }

            public void setIs_senior_member(int is_senior_member) {
                this.is_senior_member = is_senior_member;
            }

            public List<?> getAttentions() {
                return attentions;
            }

            public void setAttentions(List<?> attentions) {
                this.attentions = attentions;
            }

            public static class LevelInfoBean {
                /**
                 * current_level : 6
                 * current_min : 0
                 * current_exp : 0
                 * next_exp : 0
                 */

                private int current_level;
                private int current_min;
                private int current_exp;
                private int next_exp;

                public int getCurrent_level() {
                    return current_level;
                }

                public void setCurrent_level(int current_level) {
                    this.current_level = current_level;
                }

                public int getCurrent_min() {
                    return current_min;
                }

                public void setCurrent_min(int current_min) {
                    this.current_min = current_min;
                }

                public int getCurrent_exp() {
                    return current_exp;
                }

                public void setCurrent_exp(int current_exp) {
                    this.current_exp = current_exp;
                }

                public int getNext_exp() {
                    return next_exp;
                }

                public void setNext_exp(int next_exp) {
                    this.next_exp = next_exp;
                }
            }

            public static class PendantBean implements Serializable {
                /**
                 * pid : 0
                 * name :
                 * image :
                 * expire : 0
                 * image_enhance :
                 * image_enhance_frame :
                 */

                private int pid;
                private String name;
                private String image;
                private int expire;
                private String image_enhance;
                private String image_enhance_frame;

                public int getPid() {
                    return pid;
                }

                public void setPid(int pid) {
                    this.pid = pid;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getImage() {
                    return image;
                }

                public void setImage(String image) {
                    this.image = image;
                }

                public int getExpire() {
                    return expire;
                }

                public void setExpire(int expire) {
                    this.expire = expire;
                }

                public String getImage_enhance() {
                    return image_enhance;
                }

                public void setImage_enhance(String image_enhance) {
                    this.image_enhance = image_enhance;
                }

                public String getImage_enhance_frame() {
                    return image_enhance_frame;
                }

                public void setImage_enhance_frame(String image_enhance_frame) {
                    this.image_enhance_frame = image_enhance_frame;
                }
            }

            public static class NameplateBean implements Serializable {
                /**
                 * nid : 1
                 * name : 黄金殿堂
                 * image : https://i0.hdslb.com/bfs/face/82896ff40fcb4e7c7259cb98056975830cb55695.png
                 * image_small : https://i1.hdslb.com/bfs/face/627e342851dfda6fe7380c2fa0cbd7fae2e61533.png
                 * level : 稀有勋章
                 * condition : 单个自制视频总播放数>=100万
                 */

                private int nid;
                private String name;
                private String image;
                private String image_small;
                private String level;
                private String condition;

                public int getNid() {
                    return nid;
                }

                public void setNid(int nid) {
                    this.nid = nid;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getImage() {
                    return image;
                }

                public void setImage(String image) {
                    this.image = image;
                }

                public String getImage_small() {
                    return image_small;
                }

                public void setImage_small(String image_small) {
                    this.image_small = image_small;
                }

                public String getLevel() {
                    return level;
                }

                public void setLevel(String level) {
                    this.level = level;
                }

                public String getCondition() {
                    return condition;
                }

                public void setCondition(String condition) {
                    this.condition = condition;
                }
            }

            public static class OfficialBean implements Serializable {
                /**
                 * role : 2
                 * title : 千雅折一湖官方账号，vlog UP主
                 * desc :
                 * type : 0
                 */

                private int role;
                private String title;
                private String desc;
                private int type;

                public int getRole() {
                    return role;
                }

                public void setRole(int role) {
                    this.role = role;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getDesc() {
                    return desc;
                }

                public void setDesc(String desc) {
                    this.desc = desc;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }
            }

            public static class OfficialVerifyBean implements Serializable {
                /**
                 * type : 0
                 * desc : 千雅折一湖官方账号，vlog UP主
                 */

                private int type;
                private String desc;

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
            }

            public static class VipBean implements Serializable {
                /**
                 * type : 2
                 * status : 1
                 * due_date : 1698681600000
                 * vip_pay_type : 0
                 * theme_type : 0
                 * label : {"path":"","text":"年度大会员","label_theme":"annual_vip","text_color":"#FFFFFF","bg_style":1,"bg_color":"#FB7299","border_color":"","use_img_label":true,"img_label_uri_hans":"","img_label_uri_hant":"","img_label_uri_hans_static":"https://i0.hdslb.com/bfs/vip/8d4f8bfc713826a5412a0a27eaaac4d6b9ede1d9.png","img_label_uri_hant_static":"https://i0.hdslb.com/bfs/activity-plat/static/20220614/e369244d0b14644f5e1a06431e22a4d5/VEW8fCC0hg.png"}
                 * avatar_subscript : 1
                 * nickname_color : #FB7299
                 * role : 3
                 * avatar_subscript_url :
                 * tv_vip_status : 0
                 * tv_vip_pay_type : 0
                 * vipType : 2
                 * vipStatus : 1
                 */

                private int type;
                private int status;
                private long due_date;
                private int vip_pay_type;
                private int theme_type;
                private LabelBean label;
                private int avatar_subscript;
                private String nickname_color;
                private int role;
                private String avatar_subscript_url;
                private int tv_vip_status;
                private int tv_vip_pay_type;
                private int vipType;
                private int vipStatus;

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public long getDue_date() {
                    return due_date;
                }

                public void setDue_date(long due_date) {
                    this.due_date = due_date;
                }

                public int getVip_pay_type() {
                    return vip_pay_type;
                }

                public void setVip_pay_type(int vip_pay_type) {
                    this.vip_pay_type = vip_pay_type;
                }

                public int getTheme_type() {
                    return theme_type;
                }

                public void setTheme_type(int theme_type) {
                    this.theme_type = theme_type;
                }

                public LabelBean getLabel() {
                    return label;
                }

                public void setLabel(LabelBean label) {
                    this.label = label;
                }

                public int getAvatar_subscript() {
                    return avatar_subscript;
                }

                public void setAvatar_subscript(int avatar_subscript) {
                    this.avatar_subscript = avatar_subscript;
                }

                public String getNickname_color() {
                    return nickname_color;
                }

                public void setNickname_color(String nickname_color) {
                    this.nickname_color = nickname_color;
                }

                public int getRole() {
                    return role;
                }

                public void setRole(int role) {
                    this.role = role;
                }

                public String getAvatar_subscript_url() {
                    return avatar_subscript_url;
                }

                public void setAvatar_subscript_url(String avatar_subscript_url) {
                    this.avatar_subscript_url = avatar_subscript_url;
                }

                public int getTv_vip_status() {
                    return tv_vip_status;
                }

                public void setTv_vip_status(int tv_vip_status) {
                    this.tv_vip_status = tv_vip_status;
                }

                public int getTv_vip_pay_type() {
                    return tv_vip_pay_type;
                }

                public void setTv_vip_pay_type(int tv_vip_pay_type) {
                    this.tv_vip_pay_type = tv_vip_pay_type;
                }

                public int getVipType() {
                    return vipType;
                }

                public void setVipType(int vipType) {
                    this.vipType = vipType;
                }

                public int getVipStatus() {
                    return vipStatus;
                }

                public void setVipStatus(int vipStatus) {
                    this.vipStatus = vipStatus;
                }

                public static class LabelBean implements Serializable {
                    /**
                     * path :
                     * text : 年度大会员
                     * label_theme : annual_vip
                     * text_color : #FFFFFF
                     * bg_style : 1
                     * bg_color : #FB7299
                     * border_color :
                     * use_img_label : true
                     * img_label_uri_hans :
                     * img_label_uri_hant :
                     * img_label_uri_hans_static : https://i0.hdslb.com/bfs/vip/8d4f8bfc713826a5412a0a27eaaac4d6b9ede1d9.png
                     * img_label_uri_hant_static : https://i0.hdslb.com/bfs/activity-plat/static/20220614/e369244d0b14644f5e1a06431e22a4d5/VEW8fCC0hg.png
                     */

                    private String path;
                    private String text;
                    private String label_theme;
                    private String text_color;
                    private int bg_style;
                    private String bg_color;
                    private String border_color;
                    private boolean use_img_label;
                    private String img_label_uri_hans;
                    private String img_label_uri_hant;
                    private String img_label_uri_hans_static;
                    private String img_label_uri_hant_static;

                    public String getPath() {
                        return path;
                    }

                    public void setPath(String path) {
                        this.path = path;
                    }

                    public String getText() {
                        return text;
                    }

                    public void setText(String text) {
                        this.text = text;
                    }

                    public String getLabel_theme() {
                        return label_theme;
                    }

                    public void setLabel_theme(String label_theme) {
                        this.label_theme = label_theme;
                    }

                    public String getText_color() {
                        return text_color;
                    }

                    public void setText_color(String text_color) {
                        this.text_color = text_color;
                    }

                    public int getBg_style() {
                        return bg_style;
                    }

                    public void setBg_style(int bg_style) {
                        this.bg_style = bg_style;
                    }

                    public String getBg_color() {
                        return bg_color;
                    }

                    public void setBg_color(String bg_color) {
                        this.bg_color = bg_color;
                    }

                    public String getBorder_color() {
                        return border_color;
                    }

                    public void setBorder_color(String border_color) {
                        this.border_color = border_color;
                    }

                    public boolean isUse_img_label() {
                        return use_img_label;
                    }

                    public void setUse_img_label(boolean use_img_label) {
                        this.use_img_label = use_img_label;
                    }

                    public String getImg_label_uri_hans() {
                        return img_label_uri_hans;
                    }

                    public void setImg_label_uri_hans(String img_label_uri_hans) {
                        this.img_label_uri_hans = img_label_uri_hans;
                    }

                    public String getImg_label_uri_hant() {
                        return img_label_uri_hant;
                    }

                    public void setImg_label_uri_hant(String img_label_uri_hant) {
                        this.img_label_uri_hant = img_label_uri_hant;
                    }

                    public String getImg_label_uri_hans_static() {
                        return img_label_uri_hans_static;
                    }

                    public void setImg_label_uri_hans_static(String img_label_uri_hans_static) {
                        this.img_label_uri_hans_static = img_label_uri_hans_static;
                    }

                    public String getImg_label_uri_hant_static() {
                        return img_label_uri_hant_static;
                    }

                    public void setImg_label_uri_hant_static(String img_label_uri_hant_static) {
                        this.img_label_uri_hant_static = img_label_uri_hant_static;
                    }
                }
            }
        }
    }
}
