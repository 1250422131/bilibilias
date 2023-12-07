package com.imcys.network.di

import com.imcys.network.repository.VideoRepository
import com.imcys.network.repository.WbiKeyRepository
import com.imcys.network.repository.danmaku.DanmakuRepository
import com.imcys.network.repository.danmaku.IDanmakuDataSources
import com.imcys.network.repository.user.IUserDataSources
import com.imcys.network.repository.user.UserRepository
import com.imcys.network.repository.video.IVideoDataSources
import com.imcys.network.repository.wbi.IWbiSignatureDataSources
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataSourcesModel {
    @Binds
    fun bindIDanmakuDataSources(danmakuRepository: DanmakuRepository): IDanmakuDataSources

    @Binds
    fun bindIVideoDataSources(videoRepository: VideoRepository): IVideoDataSources

    @Binds
    fun bindIWbiSignatureDataSources(wbiKeyRepository: WbiKeyRepository): IWbiSignatureDataSources

    @Binds
    fun bindIUserDataSources(userRepository: UserRepository): IUserDataSources
}