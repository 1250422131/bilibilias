package com.imcys.bilibilias.core.data.di

import com.imcys.bilibilias.core.data.toast.ToastMachine
import com.imcys.bilibilias.core.data.toast.ToastMachineImpl
import com.imcys.bilibilias.core.data.util.ConnectivityManagerNetworkMonitor
import com.imcys.bilibilias.core.data.util.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsNetworkMonitor(networkMonitor: ConnectivityManagerNetworkMonitor): NetworkMonitor

    @Singleton
    @Binds
    internal abstract fun bindsIToast(toastMachineImpl: ToastMachineImpl): ToastMachine
}
