package com.imcys.bilibilias.common.base.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * ![登录基本信息](https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/login/login_info.md)
 * ```json
 * {
 *     "code": 0,
 *     "message": "0",
 *     "ttl": 1,
 *     "data": {
 *         "isLogin": true,
 *         "email_verified": 1,
 *         "face": "https://i0.hdslb.com/bfs/face/aebb2639a0d47f2ce1fec0631f412eaf53d4a0be.jpg",
 *         "face_nft": 0,
 *         "face_nft_type": 0,
 *         "level_info": {
 *             "current_level": 6,
 *             "current_min": 28800,
 *             "current_exp": 52689,
 *             "next_exp": "--"
 *         },
 *         "mid": 293793435,
 *         "mobile_verified": 1,
 *         "money": 172.4,
 *         "moral": 70,
 *         "official": {
 *             "role": 0,
 *             "title": "",
 *             "desc": "",
 *             "type": -1
 *         },
 *         "officialVerify": {
 *             "type": -1,
 *             "desc": ""
 *         },
 *         "pendant": {
 *             "pid": 2511,
 *             "name": "初音未来13周年",
 *             "image": "https://i0.hdslb.com/bfs/garb/item/4f8f3f1f2d47f0dad84f66aa57acd4409ea46361.png",
 *             "expire": 0,
 *             "image_enhance": "https://i0.hdslb.com/bfs/garb/item/fe0b83b53e2342b16646f6e7a9370d8a867decdb.webp",
 *             "image_enhance_frame": "https://i0.hdslb.com/bfs/garb/item/127c507ec8448be30cf5f79500ecc6ef2fd32f2c.png"
 *         },
 *         "scores": 0,
 *         "uname": "社会易姐QwQ",
 *         "vipDueDate": 1707494400000,
 *         "vipStatus": 1,
 *         "vipType": 2,
 *         "vip_pay_type": 0,
 *         "vip_theme_type": 0,
 *         "vip_label": {
 *             "path": "",
 *             "text": "年度大会员",
 *             "label_theme": "annual_vip",
 *             "text_color": "#FFFFFF",
 *             "bg_style": 1,
 *             "bg_color": "#FB7299",
 *             "border_color": "",
 *             "use_img_label": true,
 *             "img_label_uri_hans": "",
 *             "img_label_uri_hant": "",
 *             "img_label_uri_hans_static": "https://i0.hdslb.com/bfs/vip/8d4f8bfc713826a5412a0a27eaaac4d6b9ede1d9.png",
 *             "img_label_uri_hant_static": "https://i0.hdslb.com/bfs/activity-plat/static/20220614/e369244d0b14644f5e1a06431e22a4d5/VEW8fCC0hg.png"
 *         },
 *         "vip_avatar_subscript": 1,
 *         "vip_nickname_color": "#FB7299",
 *         "vip": {
 *             "type": 2,
 *             "status": 1,
 *             "due_date": 1707494400000,
 *             "vip_pay_type": 0,
 *             "theme_type": 0,
 *             "label": {
 *                 "path": "",
 *                 "text": "年度大会员",
 *                 "label_theme": "annual_vip",
 *                 "text_color": "#FFFFFF",
 *                 "bg_style": 1,
 *                 "bg_color": "#FB7299",
 *                 "border_color": "",
 *                 "use_img_label": true,
 *                 "img_label_uri_hans": "",
 *                 "img_label_uri_hant": "",
 *                 "img_label_uri_hans_static": "https://i0.hdslb.com/bfs/vip/8d4f8bfc713826a5412a0a27eaaac4d6b9ede1d9.png",
 *                 "img_label_uri_hant_static": "https://i0.hdslb.com/bfs/activity-plat/static/20220614/e369244d0b14644f5e1a06431e22a4d5/VEW8fCC0hg.png"
 *             },
 *             "avatar_subscript": 1,
 *             "nickname_color": "#FB7299",
 *             "role": 3,
 *             "avatar_subscript_url": "",
 *             "tv_vip_status": 0,
 *             "tv_vip_pay_type": 0,
 *             "tv_due_date": 1640793600
 *         },
 *         "wallet": {
 *             "mid": 293793435,
 *             "bcoin_balance": 5,
 *             "coupon_balance": 5,
 *             "coupon_due_time": 0
 *         },
 *         "has_shop": true,
 *         "shop_url": "https://gf.bilibili.com?msource=main_station",
 *         "allowance_count": 0,
 *         "answer_status": 0,
 *         "is_senior_member": 1,
 *         "wbi_img": {
 *             "img_url": "https://i0.hdslb.com/bfs/wbi/653657f524a547ac981ded72ea172057.png",
 *             "sub_url": "https://i0.hdslb.com/bfs/wbi/6e4909c702f846728e64f6007736a338.png"
 *         },
 *         "is_jury": false
 *     }
 * }
 * ```
 */
@Serializable
data class UserNav(
    @SerialName("allowance_count")
    val allowanceCount: Int = 0, // 0
    @SerialName("answer_status")
    val answerStatus: Int = 0, // 0
    @SerialName("email_verified")
    val emailVerified: Int = 0, // 1
    @SerialName("face")
    val face: String = "", // https://i0.hdslb.com/bfs/face/aebb2639a0d47f2ce1fec0631f412eaf53d4a0be.jpg
    @SerialName("face_nft")
    val faceNft: Int = 0, // 0
    @SerialName("face_nft_type")
    val faceNftType: Int = 0, // 0
    @SerialName("has_shop")
    val hasShop: Boolean = false, // true
    @SerialName("is_jury")
    val isJury: Boolean = false, // false
    @SerialName("isLogin")
    val isLogin: Boolean = false, // true
    @SerialName("is_senior_member")
    val isSeniorMember: Int = 0, // 1
    @SerialName("level_info")
    val levelInfo: LevelInfo = LevelInfo(),
    @SerialName("mid")
    val mid: Int = 0, // 293793435
    @SerialName("mobile_verified")
    val mobileVerified: Int = 0, // 1
    @SerialName("money")
    val money: Float = 0.0f, // 172.4
    @SerialName("moral")
    val moral: Int = 0, // 70
    @SerialName("official")
    val official: Official = Official(),
    @SerialName("officialVerify")
    val officialVerify: OfficialVerify = OfficialVerify(),
    @SerialName("pendant")
    val pendant: Pendant = Pendant(),
    @SerialName("scores")
    val scores: Int = 0, // 0
    @SerialName("shop_url")
    val shopUrl: String = "", // https://gf.bilibili.com?msource=main_station
    @SerialName("uname")
    val uname: String = "", // 社会易姐QwQ
    @SerialName("vip")
    val vip: Vip = Vip(),
    @SerialName("vip_avatar_subscript")
    val vipAvatarSubscript: Int = 0, // 1
    @SerialName("vipDueDate")
    val vipDueDate: Long = 0, // 1707494400000
    @SerialName("vip_label")
    val vipLabel: VipLabel = VipLabel(),
    @SerialName("vip_nickname_color")
    val vipNicknameColor: String = "", // #FB7299
    @SerialName("vip_pay_type")
    val vipPayType: Int = 0, // 0
    @SerialName("vipStatus")
    val vipStatus: Int = 0, // 1
    @SerialName("vip_theme_type")
    val vipThemeType: Int = 0, // 0
    @SerialName("vipType")
    val vipType: Int = 0, // 2
    @SerialName("wallet")
    val wallet: Wallet = Wallet(),
    @SerialName("wbi_img")
    val wbiImg: WbiImg = WbiImg()
) {
    @Serializable
    data class LevelInfo(
        @SerialName("current_exp")
        val currentExp: Int = 0, // 52689
        @SerialName("current_level")
        val currentLevel: Int = 0, // 6
        @SerialName("current_min")
        val currentMin: Int = 0, // 28800
        @SerialName("next_exp")
        val nextExp: String = "" // --
    )

    @Serializable
    data class Official(
        @SerialName("desc")
        val desc: String = "",
        @SerialName("role")
        val role: Int = 0, // 0
        @SerialName("title")
        val title: String = "",
        @SerialName("type")
        val type: Int = 0 // -1
    )

    @Serializable
    data class OfficialVerify(
        @SerialName("desc")
        val desc: String = "",
        @SerialName("type")
        val type: Int = 0 // -1
    )

    @Serializable
    data class Pendant(
        @SerialName("expire")
        val expire: Int = 0, // 0
        @SerialName("image")
        val image: String = "", // https://i0.hdslb.com/bfs/garb/item/4f8f3f1f2d47f0dad84f66aa57acd4409ea46361.png
        @SerialName("image_enhance")
        val imageEnhance: String = "", // https://i0.hdslb.com/bfs/garb/item/fe0b83b53e2342b16646f6e7a9370d8a867decdb.webp
        @SerialName("image_enhance_frame")
        val imageEnhanceFrame: String = "", // https://i0.hdslb.com/bfs/garb/item/127c507ec8448be30cf5f79500ecc6ef2fd32f2c.png
        @SerialName("name")
        val name: String = "", // 初音未来13周年
        @SerialName("pid")
        val pid: Int = 0 // 2511
    )

    @Serializable
    data class Vip(
        @SerialName("avatar_subscript")
        val avatarSubscript: Int = 0, // 1
        @SerialName("avatar_subscript_url")
        val avatarSubscriptUrl: String = "",
        @SerialName("due_date")
        val dueDate: Long = 0, // 1707494400000
        @SerialName("label")
        val label: Label = Label(),
        @SerialName("nickname_color")
        val nicknameColor: String = "", // #FB7299
        @SerialName("role")
        val role: Int = 0, // 3
        @SerialName("status")
        val status: Int = 0, // 1
        @SerialName("theme_type")
        val themeType: Int = 0, // 0
        @SerialName("tv_due_date")
        val tvDueDate: Int = 0, // 1640793600
        @SerialName("tv_vip_pay_type")
        val tvVipPayType: Int = 0, // 0
        @SerialName("tv_vip_status")
        val tvVipStatus: Int = 0, // 0
        @SerialName("type")
        val type: Int = 0, // 2
        @SerialName("vip_pay_type")
        val vipPayType: Int = 0 // 0
    ) {
        @Serializable
        data class Label(
            @SerialName("bg_color")
            val bgColor: String = "", // #FB7299
            @SerialName("bg_style")
            val bgStyle: Int = 0, // 1
            @SerialName("border_color")
            val borderColor: String = "",
            @SerialName("img_label_uri_hans")
            val imgLabelUriHans: String = "",
            @SerialName("img_label_uri_hans_static")
            val imgLabelUriHansStatic: String = "", // https://i0.hdslb.com/bfs/vip/8d4f8bfc713826a5412a0a27eaaac4d6b9ede1d9.png
            @SerialName("img_label_uri_hant")
            val imgLabelUriHant: String = "",
            @SerialName("img_label_uri_hant_static")
            val imgLabelUriHantStatic: String = "", // https://i0.hdslb.com/bfs/activity-plat/static/20220614/e369244d0b14644f5e1a06431e22a4d5/VEW8fCC0hg.png
            @SerialName("label_theme")
            val labelTheme: String = "", // annual_vip
            @SerialName("path")
            val path: String = "",
            @SerialName("text")
            val text: String = "", // 年度大会员
            @SerialName("text_color")
            val textColor: String = "", // #FFFFFF
            @SerialName("use_img_label")
            val useImgLabel: Boolean = false // true
        )
    }

    @Serializable
    data class VipLabel(
        @SerialName("bg_color")
        val bgColor: String = "", // #FB7299
        @SerialName("bg_style")
        val bgStyle: Int = 0, // 1
        @SerialName("border_color")
        val borderColor: String = "",
        @SerialName("img_label_uri_hans")
        val imgLabelUriHans: String = "",
        @SerialName("img_label_uri_hans_static")
        val imgLabelUriHansStatic: String = "", // https://i0.hdslb.com/bfs/vip/8d4f8bfc713826a5412a0a27eaaac4d6b9ede1d9.png
        @SerialName("img_label_uri_hant")
        val imgLabelUriHant: String = "",
        @SerialName("img_label_uri_hant_static")
        val imgLabelUriHantStatic: String = "", // https://i0.hdslb.com/bfs/activity-plat/static/20220614/e369244d0b14644f5e1a06431e22a4d5/VEW8fCC0hg.png
        @SerialName("label_theme")
        val labelTheme: String = "", // annual_vip
        @SerialName("path")
        val path: String = "",
        @SerialName("text")
        val text: String = "", // 年度大会员
        @SerialName("text_color")
        val textColor: String = "", // #FFFFFF
        @SerialName("use_img_label")
        val useImgLabel: Boolean = false // true
    )

    @Serializable
    data class Wallet(
        @SerialName("bcoin_balance")
        val bcoinBalance: Float = 0f, // 5
        @SerialName("coupon_balance")
        val couponBalance: Int = 0, // 5
        @SerialName("coupon_due_time")
        val couponDueTime: Int = 0, // 0
        @SerialName("mid")
        val mid: Long = 0 // 293793435
    )

    @Serializable
    data class WbiImg(
        @SerialName("img_url")
        val imgUrl: String = "", // https://i0.hdslb.com/bfs/wbi/653657f524a547ac981ded72ea172057.png
        @SerialName("sub_url")
        val subUrl: String = "" // https://i0.hdslb.com/bfs/wbi/6e4909c702f846728e64f6007736a338.png
    )

    @Transient
    val imgKey = wbiImg.imgUrl.replace(".png", "").split('/').last()

    @Transient
    val subKey = wbiImg.subUrl.replace(".png", "").split('/').last()
}
