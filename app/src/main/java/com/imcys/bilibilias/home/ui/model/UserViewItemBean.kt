package com.imcys.bilibilias.home.ui.model

import com.imcys.bilibilias.common.base.model.UserCardBean
import com.imcys.bilibilias.common.base.model.UserSpaceInformation

data class UserViewItemBean(
    val type: Int,
    val userSpaceInformation: UserSpaceInformation? = null,
    val userCardBean: UserCardBean? = null,
    val upStatBeam: UpStatBean? = null,
)