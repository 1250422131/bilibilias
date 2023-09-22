package com.imcys.bilibilias.common.base.model

import kotlinx.serialization.Serializable

/**
 * 番剧剧集明细
 *
 * ![获取剧集明细（web端）（ssid/epid方式）](https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/bangumi/info.md#%E8%8E%B7%E5%8F%96%E5%89%A7%E9%9B%86%E6%98%8E%E7%BB%86web%E7%AB%AFssidepid%E6%96%B9%E5%BC%8F)
 */
@Serializable
class BangumiSeasonBean {
    val code = 0
    val message: String = ""
    val result: ResultBean = ResultBean()

    @Serializable class ResultBean {
        val activity: ActivityBean = ActivityBean()
        val alias: String = ""
        val bkg_cover: String = ""
        val cover: String = ""
        val evaluate: String = ""
        val freya: FreyaBean = FreyaBean()
        val jp_title: String = ""
        val link: String = ""
        val media_id = 0
        val mode = 0
        val new_ep: NewEpBean = NewEpBean()
        val payment: PaymentBean = PaymentBean()
        val positive: PositiveBean = PositiveBean()
        val publish: PublishBean = PublishBean()
        val rating: RatingBean = RatingBean()
        val record: String = ""
        val rights: RightsBean = RightsBean()
        val season_id = 0
        val season_title: String = ""
        val series: SeriesBean = SeriesBean()
        val share_copy: String = ""
        val share_sub_title: String = ""
        val share_url: String = ""
        val show: ShowBean = ShowBean()
        val show_season_type = 0
        val square_cover: String = ""
        val stat: StatBean = StatBean()
        val status = 0
        val subtitle: String = ""
        val title: String = ""
        val total = 0
        val type = 0
        val up_info: UpInfoBean = UpInfoBean()
        val user_status: UserStatusBean = UserStatusBean()
        val areas: List<AreasBean> = emptyList()
        val episodes: List<EpisodesBean> = emptyList()

        @Serializable class ActivityBean {
            /**
             * head_bg_url :
             * id : 1634
             * title : 三体播放页会员购装扮中插
             */
            val head_bg_url: String = ""
            val id = 0
            val title: String = ""
        }

        @Serializable class FreyaBean {
            /**
             * bubble_desc : 你收到1个打招呼
             * bubble_show_cnt : 10000
             * icon_show : 1
             */
            val bubble_desc: String = ""
            val bubble_show_cnt = 0
            val icon_show = 0
        }

        @Serializable class NewEpBean {
            /**
             * desc : 连载中, 每周六11点，第5集起会员专享
             * id : 704475
             * is_new : 1
             * title : 3
             */
            val desc: String = ""
            val id = 0
            val is_new = 0
            val title: String = ""
        }

        @Serializable class PaymentBean {
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
            val discount = 0
            val pay_type: PayTypeBean = PayTypeBean()
            val price: String = ""
            val promotion: String = ""
            val tip: String = ""
            val view_start_time = 0
            val vip_discount = 0
            val vip_first_promotion: String = ""
            val vip_promotion: String = ""

            @Serializable class PayTypeBean {
                /**
                 * allow_discount : 0
                 * allow_pack : 0
                 * allow_ticket : 0
                 * allow_time_limit : 0
                 * allow_vip_discount : 0
                 * forbid_bb : 0
                 */
                val allow_discount = 0
                val allow_pack = 0
                val allow_ticket = 0
                val allow_time_limit = 0
                val allow_vip_discount = 0
                val forbid_bb = 0
            }
        }

        @Serializable class PositiveBean {
            /**
             * id : 35659
             * title : 正片
             */
            val id = 0
            val title: String = ""
        }

        @Serializable class PublishBean {
            /**
             * is_finish : 0
             * is_started : 1
             * pub_time : 2022-12-10 11:00:00
             * pub_time_show : 12月10日11:00
             * unknow_pub_date : 0
             * weekday : 0
             */
            val is_finish = 0
            val is_started = 0
            val pub_time: String = ""
            val pub_time_show: String = ""
            val unknow_pub_date = 0
            val weekday = 0
        }

        @Serializable class RatingBean {
            /**
             * count : 7575
             * score : 8.2
             */
            val count = 0
            val score = 0.0
        }

        @Serializable class RightsBean {
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
            val allow_bp = 0
            val allow_bp_rank = 0
            val allow_download = 0
            val allow_review = 0
            val area_limit = 0
            val ban_area_show = 0
            val can_watch = 0
            val copyright: String = ""
            val forbid_pre = 0
            val freya_white = 0
            val is_cover_show = 0
            val is_preview = 0
            val only_vip_download = 0
            val resource: String = ""
            val watch_platform = 0
        }

        @Serializable class SeriesBean {
            /**
             * display_type : 0
             * series_id : 4260
             * series_title : 三体
             */
            val display_type = 0
            val series_id = 0
            val series_title: String = ""
        }

        @Serializable class ShowBean {
            /**
             * wide_screen : 0
             */
            val wide_screen = 0
        }

        @Serializable class StatBean {
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
            val coins = 0
            val danmakus = 0
            val favorite = 0
            val favorites = 0
            val likes = 0
            val reply = 0
            val share = 0
            val views = 0
        }

        @Serializable class UpInfoBean {
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
            val avatar: String = ""
            val avatar_subscript_url: String = ""
            val follower = 0
            val is_follow = 0
            val mid: Long = 0
            val nickname_color: String = ""
            val pendant: PendantBean = PendantBean()
            val theme_type = 0
            val uname: String = ""
            val verify_type = 0
            val vip_label: VipLabelBean = VipLabelBean()
            val vip_status = 0
            val vip_type = 0

            @Serializable class PendantBean {
                /**
                 * image : https://i2.hdslb.com/bfs/garb/item/4ab1a5a6e07a99e649cde625c06eeb1c15585156.png
                 * name : 罗小黑战记
                 * pid : 5108
                 */
                val image: String = ""
                val name: String = ""
                val pid = 0
            }

            @Serializable class VipLabelBean {
                /**
                 * bg_color : #FB7299
                 * bg_style : 1
                 * border_color :
                 * text : 十年大会员
                 * text_color : #FFFFFF
                 */
                val bg_color: String = ""
                val bg_style = 0
                val border_color: String = ""
                val text: String = ""
                val text_color: String = ""
            }
        }

        @Serializable class UserStatusBean {
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
            val area_limit = 0
            val ban_area_show = 0
            val follow = 0
            val follow_status = 0
            val login = 0
            val pay = 0
            val pay_pack_paid = 0
            val sponsor = 0
        }

        @Serializable class AreasBean {
            /**
             * id : 1
             * name : 中国大陆
             */
            val id = 0
            val name: String = ""
        }

        @Serializable class EpisodesBean {
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
            @JvmField
            val checkState = 0

            @JvmField
            val selected = 0
            val aid: Long = 0

            @JvmField
            val badge: String = ""
            val badge_info: BadgeInfoBean = BadgeInfoBean()
            val badge_type = 0
            val bvid: String = ""
            val cid: Long = 0
            val cover: String = ""
            val dimension: DimensionBean = DimensionBean()
            val duration = 0
            val from: String = ""
            val id: Long = 0
            val isIs_view_hide = false
            val link: String = ""

            @JvmField
            val long_title: String = ""
            val pub_time = 0
            val pv = 0
            val release_date: String = ""
            val rights: RightsBeanX = RightsBeanX()
            val share_copy: String = ""
            val share_url: String = ""
            val short_link: String = ""
            val status = 0
            val subtitle: String = ""
            val title: String = ""
            val vid: String = ""

            @Serializable class BadgeInfoBean {
                /**
                 * bg_color : #FB7299
                 * bg_color_night : #BB5B76
                 * text :
                 */
                val bg_color: String = ""
                val bg_color_night: String = ""
                val text: String = ""
            }

            @Serializable class DimensionBean {
                /**
                 * height : 2160
                 * rotate : 0
                 * width : 3840
                 */
                val height = 0
                val rotate = 0
                val width = 0
            }

            @Serializable class RightsBeanX {
                /**
                 * allow_demand : 0
                 * allow_dm : 1
                 * allow_download : 1
                 * area_limit : 0
                 */
                val allow_demand = 0
                val allow_dm = 0
                val allow_download = 0
                val area_limit = 0
            }
        }

        @Serializable class SeasonsBean {
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
            val badge: String = ""
            val badge_info: BadgeInfoBeanX = BadgeInfoBeanX()
            val badge_type = 0
            val cover: String = ""
            val horizontal_cover_1610: String = ""
            val horizontal_cover_169: String = ""
            val media_id = 0
            val new_ep: NewEpBeanX = NewEpBeanX()
            val season_id = 0
            val season_title: String = ""
            val season_type = 0
            val stat: StatBeanX = StatBeanX()

            @Serializable class BadgeInfoBeanX {
                /**
                 * bg_color : #00C0FF
                 * bg_color_night : #0B91BE
                 * text : 出品
                 */
                val bg_color: String = ""
                val bg_color_night: String = ""
                val text: String = ""
            }

            @Serializable class NewEpBeanX {
                /**
                 * cover : http://i0.hdslb.com/bfs/archive/487cbe9ca8f35dc043a907c5c81097c1ec3da7db.jpg
                 * id : 704475
                 * index_show : 更新至第3话
                 */
                val cover: String = ""
                val id = 0
                val index_show: String = ""
            }

            @Serializable class StatBeanX {
                /**
                 * favorites : 6512751
                 * series_follow : 6521332
                 * views : 161123948
                 */
                val favorites = 0
                val series_follow = 0
                val views = 0
            }
        }

        @Serializable class SectionBean {
            val attr = 0
            val episode_id = 0
            val id = 0
            val title: String = ""
            val type = 0
            val episodes: List<EpisodesBeanX> = emptyList()

            @Serializable class EpisodesBeanX {
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
                val aid: Long = 0
                val badge: String = ""
                val badge_info: BadgeInfoBeanXX = BadgeInfoBeanXX()
                val badge_type = 0
                val bvid: String = ""
                val cid: Long = 0
                val cover: String = ""
                val dimension: DimensionBeanX = DimensionBeanX()
                val duration = 0
                val from: String = ""
                val id = 0
                val isIs_view_hide = false
                val link: String = ""
                val long_title: String = ""
                val pub_time = 0
                val pv = 0
                val release_date: String = ""
                val rights: RightsBeanXX = RightsBeanXX()
                val share_copy: String = ""
                val share_url: String = ""
                val short_link: String = ""
                val stat: StatBeanXX = StatBeanXX()
                val status = 0
                val subtitle: String = ""
                val title: String = ""
                val vid: String = ""

                @Serializable class BadgeInfoBeanXX {
                    /**
                     * bg_color : #FB7299
                     * bg_color_night : #BB5B76
                     * text :
                     */
                    val bg_color: String = ""
                    val bg_color_night: String = ""
                    val text: String = ""
                }

                @Serializable class DimensionBeanX {
                    /**
                     * height : 1080
                     * rotate : 0
                     * width : 1920
                     */
                    val height = 0
                    val rotate = 0
                    val width = 0
                }

                @Serializable class RightsBeanXX {
                    /**
                     * allow_demand : 0
                     * allow_dm : 1
                     * allow_download : 1
                     * area_limit : 0
                     */
                    val allow_demand = 0
                    val allow_dm = 0
                    val allow_download = 0
                    val area_limit = 0
                }

                @Serializable class StatBeanXX {
                    /**
                     * coin : 7039
                     * danmakus : 442
                     * likes : 25510
                     * play : 1513633
                     * reply : 1566
                     */
                    val coin = 0
                    val danmakus = 0
                    val likes = 0
                    val play = 0
                    val reply = 0
                }
            }
        }
    }
}
