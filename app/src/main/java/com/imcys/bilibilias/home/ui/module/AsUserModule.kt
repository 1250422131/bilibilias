package com.imcys.bilibilias.home.ui.module

import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.app.BaseApplication.Companion.mid
import com.imcys.bilibilias.common.base.model.user.AsUser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class) // 以告知 Hilt 每个模块将用在或安装在哪个 Android 类中。
@Module
object AsUserModule {
    @Provides
    fun providesAsUser(): AsUser {
        return run {
            val kv = BaseApplication.dataKv
            AsUser().apply {
                cookie = kv.decodeString("cookies", "")!!
                sessdata = kv.decodeString("SESSDATA", "")!!
                biliJct = kv.decodeString("bili_jct", "")!!
                mid = kv.decodeLong("mid", 0)
                asCookie = kv.decodeString("as_cookie", "")!!
            }
        }
    }
}