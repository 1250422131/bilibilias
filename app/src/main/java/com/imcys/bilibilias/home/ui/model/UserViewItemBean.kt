package com.imcys.bilibilias.home.ui.model

data class UserViewItemBean(
    val type: Int,
    val userBaseBean: UserBaseBean? = null,
    val userCardBean: UserCardBean? = null,
    val upStatBeam: UpStatBeam? = null,
)