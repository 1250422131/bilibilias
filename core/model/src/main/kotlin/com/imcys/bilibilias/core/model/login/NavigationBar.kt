package com.imcys.bilibilias.core.model.login

import com.imcys.bilibilias.core.model.user.LevelInfo
import com.imcys.bilibilias.core.model.user.Official
import com.imcys.bilibilias.core.model.user.Pendant
import com.imcys.bilibilias.core.model.user.Vip
import com.imcys.bilibilias.core.model.user.Wallet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NavigationBar(
    @SerialName("allowance_count")
    val allowanceCount: Int = 0,
    @SerialName("answer_status")
    val answerStatus: Int = 0,
    @SerialName("email_verified")
    val emailVerified: Int = 0,
    @SerialName("face")
    val face: String = "",
    @SerialName("face_nft")
    val faceNft: Int = 0,
    @SerialName("face_nft_type")
    val faceNftType: Int = 0,
    @SerialName("has_shop")
    val hasShop: Boolean = false,
    @SerialName("is_jury")
    val isJury: Boolean = false,
    @SerialName("isLogin")
    val isLogin: Boolean = false,
    @SerialName("is_senior_member")
    val isSeniorMember: Int = 0,
    @SerialName("level_info")
    val levelInfo: LevelInfo = LevelInfo(),
    @SerialName("mid")
    val mid: Long = 0,
    @SerialName("mobile_verified")
    val mobileVerified: Int = 0,
    @SerialName("money")
    val money: Double = 0.0,
    @SerialName("moral")
    val moral: Int = 0,
    @SerialName("official")
    val official: Official = Official(),
    @SerialName("pendant")
    val pendant: Pendant = Pendant(),
    @SerialName("scores")
    val scores: Int = 0,
    @SerialName("shop_url")
    val shopUrl: String = "",
    @SerialName("uname")
    val uname: String = "",
    @SerialName("vip")
    val vip: Vip = Vip(),
    @SerialName("vip_avatar_subscript")
    val vipAvatarSubscript: Int = 0,
    @SerialName("vipDueDate")
    val vipDueDate: Long = 0,
    @SerialName("vip_nickname_color")
    val vipNicknameColor: String = "",
    @SerialName("vip_pay_type")
    val vipPayType: Int = 0,
    @SerialName("vipStatus")
    val vipStatus: Int = 0,
    @SerialName("vip_theme_type")
    val vipThemeType: Int = 0,
    @SerialName("vipType")
    val vipType: Int = 0,
    @SerialName("wallet")
    val wallet: Wallet = Wallet(),
    @SerialName("wbi_img")
    val wbiImg: WbiImg = WbiImg()
) {
    @Serializable
    data class WbiImg(
        @SerialName("img_url")
        val imgUrl: String = "",
        @SerialName("sub_url")
        val subUrl: String = ""
    )

    val imgKey = wbiImg.imgUrl.replace(".png", "").split('/').last()
    val subKey = wbiImg.subUrl.replace(".png", "").split('/').last()
}
