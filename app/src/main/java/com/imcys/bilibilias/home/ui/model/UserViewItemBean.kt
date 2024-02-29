package com.imcys.bilibilias.home.ui.model

data class UserViewItemBean(
    val type: Int,
    val userBaseBean: UserBaseBean = UserBaseBean(),
    val userCardBean: UserCardBean = UserCardBean(),
    val upStatBeam: UpStatBeam = UpStatBeam(),
)
