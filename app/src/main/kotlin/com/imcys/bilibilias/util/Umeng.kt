package com.imcys.bilibilias.util

import android.content.Context
import com.imcys.bilibilias.core.common.network.di.ApplicationScope
import javax.inject.Inject

class Umeng @Inject constructor(@ApplicationScope private val context: Context) {
    operator fun invoke() {
//       UMConfigure.init(context, "664b7e85940d5a4c495ab9a3", null, UMConfigure.DEVICE_TYPE_PHONE, null)
//       UMConfigure.setLogEnabled(true)
//       MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
    }
}
