package com.imcys.bilibilias.home.ui.model

import com.imcys.bilibilias.home.ui.activity.DedicateActivity

data class DonateViewBean(
    val type: Int,
    val oldDonateBean: OldDonateBean? = null,
    val tipBean: TipBean<DedicateActivity>? = null,
)