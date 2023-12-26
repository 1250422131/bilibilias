package com.imcys.network.di

import com.imcys.network.download.DownloadManage
import com.imcys.network.download.IDownloadManage
import com.imcys.network.repository.auth.AuthRepository
import com.imcys.network.repository.auth.IAuthDataSources
import com.imcys.network.repository.danmaku.DanmakuRepository
import com.imcys.network.repository.danmaku.IDanmakuDataSources
import com.imcys.network.repository.user.IUserDataSources
import com.imcys.network.repository.user.UserRepository
import com.imcys.network.repository.video.IVideoDataSources
import com.imcys.network.repository.video.VideoRepository
import com.imcys.network.repository.wbi.IWbiSignatureDataSources
import com.imcys.network.repository.wbi.WbiKeyRepository
import com.imcys.network.utils.ConnectivityManagerNetworkMonitor
import com.imcys.network.utils.INetworkMonitor
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

    @Binds
    fun bindIAuthDataSources(authRepository: AuthRepository): IAuthDataSources

    @Binds
    fun bindINetworkMonitor(connectivityManagerNetworkMonitor: ConnectivityManagerNetworkMonitor): INetworkMonitor

    @Binds
    fun bindIDownloadManage(downloadManage: DownloadManage): IDownloadManage
}