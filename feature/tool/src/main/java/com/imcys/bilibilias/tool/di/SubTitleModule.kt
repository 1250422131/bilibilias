package com.imcys.bilibilias.tool.di

import com.imcys.bilibilias.tool.danmaku.DanmakuXMLParse
import com.imcys.bilibilias.tool.danmaku.IDanmakuParse
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SubTitleModule {
    @Binds
    fun bindsIDanmakuParse(
        danmakuXMLParse: DanmakuXMLParse
    ): IDanmakuParse
}
