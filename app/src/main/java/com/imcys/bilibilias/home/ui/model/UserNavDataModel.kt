package com.imcys.bilibilias.home.ui.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserNavDataModel(
    @SerialName("code")
    val code: Int, // 0
    @SerialName("data")
    val `data`: Data,
    @SerialName("message")
    val message: String, // 0
    @SerialName("ttl")
    val ttl: Int, // 1
) {
    @Serializable
    data class Data(
//        @SerialName("allowance_count")
//        val allowanceCount: Int, // 0
        @SerialName("answer_status")
        val answerStatus: Int, // 0
        @SerialName("email_verified")
        val emailVerified: Int, // 1
        @SerialName("face")
        val face: String, // https://i1.hdslb.com/bfs/face/6323fa4fdbd8cfa9d448cc9a3a0c32394bf34426.jpg
        @SerialName("face_nft")
        val faceNft: Int, // 1
        @SerialName("face_nft_type")
        val faceNftType: Int, // 1
        @SerialName("has_shop")
        val hasShop: Boolean, // false
        @SerialName("is_jury")
        val isJury: Boolean, // false
        @SerialName("isLogin")
        val isLogin: Boolean, // true
        @SerialName("is_senior_member")
        val isSeniorMember: Int, // 1
        @SerialName("level_info")
        val levelInfo: LevelInfo,
        @SerialName("mid")
        val mid: Long, // 351201307
        @SerialName("mobile_verified")
        val mobileVerified: Int, // 1
        @SerialName("money")
        val money: Double, // 3563.6
        @SerialName("moral")
        val moral: Int, // 70
        @SerialName("official")
        val official: Official,
        @SerialName("officialVerify")
        val officialVerify: OfficialVerify,
        @SerialName("pendant")
        val pendant: Pendant,
        @SerialName("scores")
        val scores: Int, // 0
        @SerialName("shop_url")
        val shopUrl: String,
        @SerialName("uname")
        val uname: String, // 萌新杰少
        @SerialName("vip")
        val vip: Vip,
        @SerialName("vip_avatar_subscript")
        val vipAvatarSubscript: Int, // 0
        @SerialName("vipDueDate")
        val vipDueDate: Long, // 1637769600000
        @SerialName("vip_label")
        val vipLabel: VipLabel,
        @SerialName("vip_nickname_color")
        val vipNicknameColor: String,
        @SerialName("vip_pay_type")
        val vipPayType: Int, // 0
        @SerialName("vipStatus")
        val vipStatus: Int, // 0
        @SerialName("vip_theme_type")
        val vipThemeType: Int, // 0
        @SerialName("vipType")
        val vipType: Int, // 1
        @SerialName("wallet")
        val wallet: Wallet,
        @SerialName("wbi_img")
        val wbiImg: WbiImg,
    ) {
        @Serializable
        data class LevelInfo(
            @SerialName("current_exp")
            val currentExp: Int, // 62774
            @SerialName("current_level")
            val currentLevel: Int, // 6
            @SerialName("current_min")
            val currentMin: Int, // 28800
            @SerialName("next_exp")
            val nextExp: String, // --
        )

        @Serializable
        data class Official(
            @SerialName("desc")
            val desc: String,
            @SerialName("role")
            val role: Int, // 0
            @SerialName("title")
            val title: String,
            @SerialName("type")
            val type: Int, // -1
        )

        @Serializable
        data class OfficialVerify(
            @SerialName("desc")
            val desc: String,
            @SerialName("type")
            val type: Int, // -1
        )

        @Serializable
        data class Pendant(
            @SerialName("expire")
            val expire: Int, // 0
            @SerialName("image")
            val image: String, // https://i1.hdslb.com/bfs/garb/item/c90b1b7d971d0a6f92f10ac3b9ae3c7e80fe2dc8.png
            @SerialName("image_enhance")
            val imageEnhance: String, // https://i1.hdslb.com/bfs/garb/item/c90b1b7d971d0a6f92f10ac3b9ae3c7e80fe2dc8.png
            @SerialName("image_enhance_frame")
            val imageEnhanceFrame: String,
            @SerialName("name")
            val name: String, // 拜年祭粉丝专鼠
            @SerialName("pid")
            val pid: Int, // 1416
        )

        @Serializable
        data class Vip(
            @SerialName("avatar_subscript")
            val avatarSubscript: Int, // 0
            @SerialName("avatar_subscript_url")
            val avatarSubscriptUrl: String,
            @SerialName("due_date")
            val dueDate: Long, // 1637769600000
            @SerialName("label")
            val label: Label,
            @SerialName("nickname_color")
            val nicknameColor: String,
            @SerialName("role")
            val role: Int, // 0
            @SerialName("status")
            val status: Int, // 0
            @SerialName("theme_type")
            val themeType: Int, // 0
            @SerialName("tv_vip_pay_type")
            val tvVipPayType: Int, // 0
            @SerialName("tv_vip_status")
            val tvVipStatus: Int, // 0
            @SerialName("type")
            val type: Int, // 1
            @SerialName("vip_pay_type")
            val vipPayType: Int, // 0
        ) {
            @Serializable
            data class Label(
                @SerialName("bg_color")
                val bgColor: String,
                @SerialName("bg_style")
                val bgStyle: Int, // 0
                @SerialName("border_color")
                val borderColor: String,
                @SerialName("img_label_uri_hans")
                val imgLabelUriHans: String,
                @SerialName("img_label_uri_hans_static")
                val imgLabelUriHansStatic: String, // https://i0.hdslb.com/bfs/vip/d7b702ef65a976b20ed854cbd04cb9e27341bb79.png
                @SerialName("img_label_uri_hant")
                val imgLabelUriHant: String,
                @SerialName("img_label_uri_hant_static")
                val imgLabelUriHantStatic: String, // https://i0.hdslb.com/bfs/activity-plat/static/20220614/e369244d0b14644f5e1a06431e22a4d5/KJunwh19T5.png
                @SerialName("label_theme")
                val labelTheme: String,
                @SerialName("path")
                val path: String,
                @SerialName("text")
                val text: String,
                @SerialName("text_color")
                val textColor: String,
                @SerialName("use_img_label")
                val useImgLabel: Boolean, // true
            )
        }

        @Serializable
        data class VipLabel(
            @SerialName("bg_color")
            val bgColor: String,
            @SerialName("bg_style")
            val bgStyle: Int, // 0
            @SerialName("border_color")
            val borderColor: String,
            @SerialName("img_label_uri_hans")
            val imgLabelUriHans: String,
            @SerialName("img_label_uri_hans_static")
            val imgLabelUriHansStatic: String, // https://i0.hdslb.com/bfs/vip/d7b702ef65a976b20ed854cbd04cb9e27341bb79.png
            @SerialName("img_label_uri_hant")
            val imgLabelUriHant: String,
            @SerialName("img_label_uri_hant_static")
            val imgLabelUriHantStatic: String, // https://i0.hdslb.com/bfs/activity-plat/static/20220614/e369244d0b14644f5e1a06431e22a4d5/KJunwh19T5.png
            @SerialName("label_theme")
            val labelTheme: String,
            @SerialName("path")
            val path: String,
            @SerialName("text")
            val text: String,
            @SerialName("text_color")
            val textColor: String,
            @SerialName("use_img_label")
            val useImgLabel: Boolean, // true
        )

        @Serializable
        data class Wallet(
            @SerialName("bcoin_balance")
            val bcoinBalance: Double, // 1
            @SerialName("coupon_balance")
            val couponBalance: Double, // 0
            @SerialName("coupon_due_time")
            val couponDueTime: Double, // 0
            @SerialName("mid")
            val mid: Long, // 351201307
        )

        @Serializable
        data class WbiImg(
            @SerialName("img_url")
            val imgUrl: String, // https://i0.hdslb.com/bfs/wbi/668e41e42b0544a88cbd311d52799d9f.png
            @SerialName("sub_url")
            val subUrl: String, // https://i0.hdslb.com/bfs/wbi/ce03d395ec2346f0b84bb3c090c11ac4.png
        )
    }
}
