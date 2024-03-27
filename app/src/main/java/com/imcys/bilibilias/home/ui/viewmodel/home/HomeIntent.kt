package com.imcys.bilibilias.home.ui.viewmodel.home

import com.imcys.bilibilias.core.model.bilibilias.Banner
import com.imcys.bilibilias.core.model.bilibilias.UpdateNotice

sealed interface HomeIntent {
    data class UpdateNoticeIntent(val updateNotice: UpdateNotice) : HomeIntent
    data class BannerIntent(val banner: Banner) : HomeIntent

    data object Loading : HomeIntent
}
