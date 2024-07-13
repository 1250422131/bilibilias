package com.imcys.bilibilias.core.model.video
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class Author(
    @SerialName("birthday")
    val birthday: Int = 0,
    @SerialName("face")
    val face: String = "",
    @SerialName("in_reg_audit")
    val inRegAudit: Int = 0,
    @SerialName("is_deleted")
    val isDeleted: Int = 0,
    @SerialName("is_fake_account")
    val isFakeAccount: Int = 0,
    @SerialName("is_senior_member")
    val isSeniorMember: Int = 0,
    @SerialName("mid")
    val mid: Long = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("rank")
    val rank: Int = 0,
    @SerialName("sex")
    val sex: String = "",
    @SerialName("sign")
    val sign: String = ""
)
