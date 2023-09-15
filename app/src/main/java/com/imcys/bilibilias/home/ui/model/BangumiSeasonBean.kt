package com.imcys.bilibilias.home.ui.model

import java.io.Serializable

/**
 * @author:imcys
 * @create: 2022-12-17 15:19
 * @Description: 番剧剧集明细
 */
class BangumiSeasonBean : Serializable {
    var code = 0
    var message: String = ""
    var result: ResultBean = ResultBean()

    class ResultBean : Serializable {
        var activity: ActivityBean = ActivityBean()
        var alias: String = ""
        var bkg_cover: String = ""
        var cover: String = ""
        var evaluate: String = ""
        var freya: FreyaBean = FreyaBean()
        var jp_title: String = ""
        var link: String = ""
        var media_id = 0
        var mode = 0
        var new_ep: NewEpBean = NewEpBean()
        var payment: PaymentBean = PaymentBean()
        var positive: PositiveBean = PositiveBean()
        var publish: PublishBean = PublishBean()
        var rating: RatingBean = RatingBean()
        var record: String = ""
        var rights: RightsBean = RightsBean()
        var season_id = 0
        var season_title: String = ""
        var series: SeriesBean = SeriesBean()
        var share_copy: String = ""
        var share_sub_title: String = ""
        var share_url: String = ""
        var show: ShowBean = ShowBean()
        var show_season_type = 0
        var square_cover: String = ""
        var stat: StatBean = StatBean()
        var status = 0
        var subtitle: String = ""
        var title: String = ""
        var total = 0
        var type = 0
        var up_info: UpInfoBean = UpInfoBean()
        var user_status: UserStatusBean = UserStatusBean()
        var areas: List<AreasBean> = emptyList()
        var episodes: List<EpisodesBean> = emptyList()

        class ActivityBean : Serializable {
            /**
             * head_bg_url :
             * id : 1634
             * title : 三体播放页会员购装扮中插
             */
            var head_bg_url: String = ""
            var id = 0
            var title: String = ""
        }

        class FreyaBean : Serializable {
            /**
             * bubble_desc : 你收到1个打招呼
             * bubble_show_cnt : 10000
             * icon_show : 1
             */
            var bubble_desc: String = ""
            var bubble_show_cnt = 0
            var icon_show = 0
        }

        class NewEpBean : Serializable {
            /**
             * desc : 连载中, 每周六11点，第5集起会员专享
             * id : 704475
             * is_new : 1
             * title : 3
             */
            var desc: String = ""
            var id = 0
            var is_new = 0
            var title: String = ""
        }

        class PaymentBean : Serializable {
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
            var discount = 0
            var pay_type: PayTypeBean = PayTypeBean()
            var price: String = ""
            var promotion: String = ""
            var tip: String = ""
            var view_start_time = 0
            var vip_discount = 0
            var vip_first_promotion: String = ""
            var vip_promotion: String = ""

            class PayTypeBean : Serializable {
                /**
                 * allow_discount : 0
                 * allow_pack : 0
                 * allow_ticket : 0
                 * allow_time_limit : 0
                 * allow_vip_discount : 0
                 * forbid_bb : 0
                 */
                var allow_discount = 0
                var allow_pack = 0
                var allow_ticket = 0
                var allow_time_limit = 0
                var allow_vip_discount = 0
                var forbid_bb = 0
            }
        }

        class PositiveBean : Serializable {
            /**
             * id : 35659
             * title : 正片
             */
            var id = 0
            var title: String = ""
        }

        class PublishBean : Serializable {
            /**
             * is_finish : 0
             * is_started : 1
             * pub_time : 2022-12-10 11:00:00
             * pub_time_show : 12月10日11:00
             * unknow_pub_date : 0
             * weekday : 0
             */
            var is_finish = 0
            var is_started = 0
            var pub_time: String = ""
            var pub_time_show: String = ""
            var unknow_pub_date = 0
            var weekday = 0
        }

        class RatingBean : Serializable {
            /**
             * count : 7575
             * score : 8.2
             */
            var count = 0
            var score = 0.0
        }

        class RightsBean : Serializable {
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
            var allow_bp = 0
            var allow_bp_rank = 0
            var allow_download = 0
            var allow_review = 0
            var area_limit = 0
            var ban_area_show = 0
            var can_watch = 0
            var copyright: String = ""
            var forbid_pre = 0
            var freya_white = 0
            var is_cover_show = 0
            var is_preview = 0
            var only_vip_download = 0
            var resource: String = ""
            var watch_platform = 0
        }

        class SeriesBean : Serializable {
            /**
             * display_type : 0
             * series_id : 4260
             * series_title : 三体
             */
            var display_type = 0
            var series_id = 0
            var series_title: String = ""
        }

        class ShowBean : Serializable {
            /**
             * wide_screen : 0
             */
            var wide_screen = 0
        }

        class StatBean : Serializable {
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
            var coins = 0
            var danmakus = 0
            var favorite = 0
            var favorites = 0
            var likes = 0
            var reply = 0
            var share = 0
            var views = 0
        }

        class UpInfoBean : Serializable {
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
            var avatar: String = ""
            var avatar_subscript_url: String = ""
            var follower = 0
            var is_follow = 0
            var mid: Long = 0
            var nickname_color: String = ""
            var pendant: PendantBean = PendantBean()
            var theme_type = 0
            var uname: String = ""
            var verify_type = 0
            var vip_label: VipLabelBean = VipLabelBean()
            var vip_status = 0
            var vip_type = 0

            class PendantBean : Serializable {
                /**
                 * image : https://i2.hdslb.com/bfs/garb/item/4ab1a5a6e07a99e649cde625c06eeb1c15585156.png
                 * name : 罗小黑战记
                 * pid : 5108
                 */
                var image: String = ""
                var name: String = ""
                var pid = 0
            }

            class VipLabelBean : Serializable {
                /**
                 * bg_color : #FB7299
                 * bg_style : 1
                 * border_color :
                 * text : 十年大会员
                 * text_color : #FFFFFF
                 */
                var bg_color: String = ""
                var bg_style = 0
                var border_color: String = ""
                var text: String = ""
                var text_color: String = ""
            }
        }

        class UserStatusBean : Serializable {
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
            var area_limit = 0
            var ban_area_show = 0
            var follow = 0
            var follow_status = 0
            var login = 0
            var pay = 0
            var pay_pack_paid = 0
            var sponsor = 0
        }

        class AreasBean : Serializable {
            /**
             * id : 1
             * name : 中国大陆
             */
            var id = 0
            var name: String = ""
        }

        class EpisodesBean : Serializable {
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
            var checkState = 0

            @JvmField
            var selected = 0
            var aid: Long = 0

            @JvmField
            var badge: String = ""
            var badge_info: BadgeInfoBean = BadgeInfoBean()
            var badge_type = 0
            var bvid: String = ""
            var cid: Long = 0
            var cover: String = ""
            var dimension: DimensionBean = DimensionBean()
            var duration = 0
            var from: String = ""
            var id: Long = 0
            var isIs_view_hide = false
            var link: String = ""

            @JvmField
            var long_title: String = ""
            var pub_time = 0
            var pv = 0
            var release_date: String = ""
            var rights: RightsBeanX = RightsBeanX()
            var share_copy: String = ""
            var share_url: String = ""
            var short_link: String = ""
            var status = 0
            var subtitle: String = ""
            var title: String = ""
            var vid: String = ""

            class BadgeInfoBean : Serializable {
                /**
                 * bg_color : #FB7299
                 * bg_color_night : #BB5B76
                 * text :
                 */
                var bg_color: String = ""
                var bg_color_night: String = ""
                var text: String = ""
            }

            class DimensionBean : Serializable {
                /**
                 * height : 2160
                 * rotate : 0
                 * width : 3840
                 */
                var height = 0
                var rotate = 0
                var width = 0
            }

            class RightsBeanX : Serializable {
                /**
                 * allow_demand : 0
                 * allow_dm : 1
                 * allow_download : 1
                 * area_limit : 0
                 */
                var allow_demand = 0
                var allow_dm = 0
                var allow_download = 0
                var area_limit = 0
            }
        }

        class SeasonsBean : Serializable {
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
            var badge: String = ""
            var badge_info: BadgeInfoBeanX = BadgeInfoBeanX()
            var badge_type = 0
            var cover: String = ""
            var horizontal_cover_1610: String = ""
            var horizontal_cover_169: String = ""
            var media_id = 0
            var new_ep: NewEpBeanX = NewEpBeanX()
            var season_id = 0
            var season_title: String = ""
            var season_type = 0
            var stat: StatBeanX = StatBeanX()

            class BadgeInfoBeanX : Serializable {
                /**
                 * bg_color : #00C0FF
                 * bg_color_night : #0B91BE
                 * text : 出品
                 */
                var bg_color: String = ""
                var bg_color_night: String = ""
                var text: String = ""
            }

            class NewEpBeanX : Serializable {
                /**
                 * cover : http://i0.hdslb.com/bfs/archive/487cbe9ca8f35dc043a907c5c81097c1ec3da7db.jpg
                 * id : 704475
                 * index_show : 更新至第3话
                 */
                var cover: String = ""
                var id = 0
                var index_show: String = ""
            }

            class StatBeanX : Serializable {
                /**
                 * favorites : 6512751
                 * series_follow : 6521332
                 * views : 161123948
                 */
                var favorites = 0
                var series_follow = 0
                var views = 0
            }
        }

        class SectionBean : Serializable {
            var attr = 0
            var episode_id = 0
            var id = 0
            var title: String = ""
            var type = 0
            var episodes: List<EpisodesBeanX> = emptyList()

            class EpisodesBeanX : Serializable {
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
                var aid: Long = 0
                var badge: String = ""
                var badge_info: BadgeInfoBeanXX = BadgeInfoBeanXX()
                var badge_type = 0
                var bvid: String = ""
                var cid: Long = 0
                var cover: String = ""
                var dimension: DimensionBeanX = DimensionBeanX()
                var duration = 0
                var from: String = ""
                var id = 0
                var isIs_view_hide = false
                var link: String = ""
                var long_title: String = ""
                var pub_time = 0
                var pv = 0
                var release_date: String = ""
                var rights: RightsBeanXX = RightsBeanXX()
                var share_copy: String = ""
                var share_url: String = ""
                var short_link: String = ""
                var stat: StatBeanXX = StatBeanXX()
                var status = 0
                var subtitle: String = ""
                var title: String = ""
                var vid: String = ""

                class BadgeInfoBeanXX : Serializable {
                    /**
                     * bg_color : #FB7299
                     * bg_color_night : #BB5B76
                     * text :
                     */
                    var bg_color: String = ""
                    var bg_color_night: String = ""
                    var text: String = ""
                }

                class DimensionBeanX : Serializable {
                    /**
                     * height : 1080
                     * rotate : 0
                     * width : 1920
                     */
                    var height = 0
                    var rotate = 0
                    var width = 0
                }

                class RightsBeanXX : Serializable {
                    /**
                     * allow_demand : 0
                     * allow_dm : 1
                     * allow_download : 1
                     * area_limit : 0
                     */
                    var allow_demand = 0
                    var allow_dm = 0
                    var allow_download = 0
                    var area_limit = 0
                }

                class StatBeanXX : Serializable {
                    /**
                     * coin : 7039
                     * danmakus : 442
                     * likes : 25510
                     * play : 1513633
                     * reply : 1566
                     */
                    var coin = 0
                    var danmakus = 0
                    var likes = 0
                    var play = 0
                    var reply = 0
                }
            }
        }
    }
}
