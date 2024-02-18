package com.imcys.network.di

import com.imcys.network.repository.auth.*
import com.imcys.network.repository.danmaku.*
import com.imcys.network.repository.user.*
import com.imcys.network.repository.video.*
import com.imcys.network.repository.wbi.*
import com.imcys.network.utils.*
import dagger.*
import dagger.hilt.*
import dagger.hilt.components.*

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

    @Binds
    fun bindIAuthDataSources(authRepository: AuthRepository): IAuthDataSources

    @Binds
    fun bindINetworkMonitor(connectivityManagerNetworkMonitor: ConnectivityManagerNetworkMonitor): INetworkMonitor
}