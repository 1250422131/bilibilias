package com.imcys.bilibilias.feature.home

import com.imcys.bilibilias.core.model.bilibilias.HomeBanner
import com.imcys.bilibilias.core.model.bilibilias.UpdateNotice

data class HomeUiState(val updateNotice: UpdateNotice, val homeBanner: HomeBanner)
