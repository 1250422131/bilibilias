package com.imcys.bilibilias.core.data.di

import com.imcys.bilibilias.core.data.util.ConnectivityManagerNetworkMonitor
import com.imcys.bilibilias.core.data.util.ErrorMonitor
import com.imcys.bilibilias.core.data.util.NetworkMonitor
import com.imcys.bilibilias.core.data.util.SnackbarErrorMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsNetworkMonitor(networkMonitor: ConnectivityManagerNetworkMonitor): NetworkMonitor

    @Binds
    internal abstract fun bindsErrorMonitor(errorMonitor: SnackbarErrorMonitor): ErrorMonitor
}
