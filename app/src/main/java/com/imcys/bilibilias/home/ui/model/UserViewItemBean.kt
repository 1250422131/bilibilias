package com.imcys.bilibilias.home.ui.model

import com.imcys.model.UserCardBean
import com.imcys.model.UserSpaceInformation

data class UserViewItemBean(
    val type: Int,
    val userSpaceInformation: UserSpaceInformation? = null,
    val userCardBean: UserCardBean? = null,
    val upStatBeam: UpStatBean? = null,
)