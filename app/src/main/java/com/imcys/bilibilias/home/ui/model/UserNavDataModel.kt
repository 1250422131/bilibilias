package com.imcys.bilibilias.home.ui.model
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class UserNavDataModel(
    @SerializedName("code")
    val code: Int, // 0
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String, // 0
    @SerializedName("ttl")
    val ttl: Int // 1
) : Serializable {
    data class Data(
        @SerializedName("allowance_count")
        val allowanceCount: Int, // 0
        @SerializedName("answer_status")
        val answerStatus: Int, // 0
        @SerializedName("email_verified")
        val emailVerified: Int, // 1
        @SerializedName("face")
        val face: String, // https://i1.hdslb.com/bfs/face/6323fa4fdbd8cfa9d448cc9a3a0c32394bf34426.jpg
        @SerializedName("face_nft")
        val faceNft: Int, // 1
        @SerializedName("face_nft_type")
        val faceNftType: Int, // 1
        @SerializedName("has_shop")
        val hasShop: Boolean, // false
        @SerializedName("is_jury")
        val isJury: Boolean, // false
        @SerializedName("isLogin")
        val isLogin: Boolean, // true
        @SerializedName("is_senior_member")
        val isSeniorMember: Int, // 1
        @SerializedName("level_info")
        val levelInfo: LevelInfo,
        @SerializedName("mid")
        val mid: Long, // 351201307
        @SerializedName("mobile_verified")
        val mobileVerified: Int, // 1
        @SerializedName("money")
        val money: Double, // 3563.6
        @SerializedName("moral")
        val moral: Int, // 70
        @SerializedName("official")
        val official: Official,
        @SerializedName("officialVerify")
        val officialVerify: OfficialVerify,
        @SerializedName("pendant")
        val pendant: Pendant,
        @SerializedName("scores")
        val scores: Int, // 0
        @SerializedName("shop_url")
        val shopUrl: String,
        @SerializedName("uname")
        val uname: String, // 萌新杰少
        @SerializedName("vip")
        val vip: Vip,
        @SerializedName("vip_avatar_subscript")
        val vipAvatarSubscript: Int, // 0
        @SerializedName("vipDueDate")
        val vipDueDate: Long, // 1637769600000
        @SerializedName("vip_label")
        val vipLabel: VipLabel,
        @SerializedName("vip_nickname_color")
        val vipNicknameColor: String,
        @SerializedName("vip_pay_type")
        val vipPayType: Int, // 0
        @SerializedName("vipStatus")
        val vipStatus: Int, // 0
        @SerializedName("vip_theme_type")
        val vipThemeType: Int, // 0
        @SerializedName("vipType")
        val vipType: Int, // 1
        @SerializedName("wallet")
        val wallet: Wallet,
        @SerializedName("wbi_img")
        val wbiImg: WbiImg
    ) : Serializable {
        data class LevelInfo(
            @SerializedName("current_exp")
            val currentExp: Int, // 62774
            @SerializedName("current_level")
            val currentLevel: Int, // 6
            @SerializedName("current_min")
            val currentMin: Int, // 28800
            @SerializedName("next_exp")
            val nextExp: String // --
        ): Serializable

        data class Official(
            @SerializedName("desc")
            val desc: String,
            @SerializedName("role")
            val role: Int, // 0
            @SerializedName("title")
            val title: String,
            @SerializedName("type")
            val type: Int // -1
        ): Serializable

        data class OfficialVerify(
            @SerializedName("desc")
            val desc: String,
            @SerializedName("type")
            val type: Int // -1
        ): Serializable

        data class Pendant(
            @SerializedName("expire")
            val expire: Int, // 0
            @SerializedName("image")
            val image: String, // https://i1.hdslb.com/bfs/garb/item/c90b1b7d971d0a6f92f10ac3b9ae3c7e80fe2dc8.png
            @SerializedName("image_enhance")
            val imageEnhance: String, // https://i1.hdslb.com/bfs/garb/item/c90b1b7d971d0a6f92f10ac3b9ae3c7e80fe2dc8.png
            @SerializedName("image_enhance_frame")
            val imageEnhanceFrame: String,
            @SerializedName("name")
            val name: String, // 拜年祭粉丝专鼠
            @SerializedName("pid")
            val pid: Int // 1416
        ): Serializable

        data class Vip(
            @SerializedName("avatar_subscript")
            val avatarSubscript: Int, // 0
            @SerializedName("avatar_subscript_url")
            val avatarSubscriptUrl: String,
            @SerializedName("due_date")
            val dueDate: Long, // 1637769600000
            @SerializedName("label")
            val label: Label,
            @SerializedName("nickname_color")
            val nicknameColor: String,
            @SerializedName("role")
            val role: Int, // 0
            @SerializedName("status")
            val status: Int, // 0
            @SerializedName("theme_type")
            val themeType: Int, // 0
            @SerializedName("tv_vip_pay_type")
            val tvVipPayType: Int, // 0
            @SerializedName("tv_vip_status")
            val tvVipStatus: Int, // 0
            @SerializedName("type")
            val type: Int, // 1
            @SerializedName("vip_pay_type")
            val vipPayType: Int // 0
        ): Serializable  {
            data class Label(
                @SerializedName("bg_color")
                val bgColor: String,
                @SerializedName("bg_style")
                val bgStyle: Int, // 0
                @SerializedName("border_color")
                val borderColor: String,
                @SerializedName("img_label_uri_hans")
                val imgLabelUriHans: String,
                @SerializedName("img_label_uri_hans_static")
                val imgLabelUriHansStatic: String, // https://i0.hdslb.com/bfs/vip/d7b702ef65a976b20ed854cbd04cb9e27341bb79.png
                @SerializedName("img_label_uri_hant")
                val imgLabelUriHant: String,
                @SerializedName("img_label_uri_hant_static")
                val imgLabelUriHantStatic: String, // https://i0.hdslb.com/bfs/activity-plat/static/20220614/e369244d0b14644f5e1a06431e22a4d5/KJunwh19T5.png
                @SerializedName("label_theme")
                val labelTheme: String,
                @SerializedName("path")
                val path: String,
                @SerializedName("text")
                val text: String,
                @SerializedName("text_color")
                val textColor: String,
                @SerializedName("use_img_label")
                val useImgLabel: Boolean // true
            ): Serializable
        }

        data class VipLabel(
            @SerializedName("bg_color")
            val bgColor: String,
            @SerializedName("bg_style")
            val bgStyle: Int, // 0
            @SerializedName("border_color")
            val borderColor: String,
            @SerializedName("img_label_uri_hans")
            val imgLabelUriHans: String,
            @SerializedName("img_label_uri_hans_static")
            val imgLabelUriHansStatic: String, // https://i0.hdslb.com/bfs/vip/d7b702ef65a976b20ed854cbd04cb9e27341bb79.png
            @SerializedName("img_label_uri_hant")
            val imgLabelUriHant: String,
            @SerializedName("img_label_uri_hant_static")
            val imgLabelUriHantStatic: String, // https://i0.hdslb.com/bfs/activity-plat/static/20220614/e369244d0b14644f5e1a06431e22a4d5/KJunwh19T5.png
            @SerializedName("label_theme")
            val labelTheme: String,
            @SerializedName("path")
            val path: String,
            @SerializedName("text")
            val text: String,
            @SerializedName("text_color")
            val textColor: String,
            @SerializedName("use_img_label")
            val useImgLabel: Boolean // true
        ): Serializable

        data class Wallet(
            @SerializedName("bcoin_balance")
            val bcoinBalance: Double, // 1
            @SerializedName("coupon_balance")
            val couponBalance: Int, // 0
            @SerializedName("coupon_due_time")
            val couponDueTime: Int, // 0
            @SerializedName("mid")
            val mid: Int // 351201307
        ): Serializable

        data class WbiImg(
            @SerializedName("img_url")
            val imgUrl: String, // https://i0.hdslb.com/bfs/wbi/668e41e42b0544a88cbd311d52799d9f.png
            @SerializedName("sub_url")
            val subUrl: String // https://i0.hdslb.com/bfs/wbi/ce03d395ec2346f0b84bb3c090c11ac4.png
        ): Serializable
    }
}