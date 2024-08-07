package com.imcys.bilibilias.core.data.di

import com.imcys.bilibilias.core.data.AlwaysOnlineNetworkMonitor
import com.imcys.bilibilias.core.data.FakeErrorMonitor
import com.imcys.bilibilias.core.data.util.ErrorMonitor
import com.imcys.bilibilias.core.data.util.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class],
)
internal interface TestDataModule {
    @Binds
    fun bindsNetworkMonitor(networkMonitor: AlwaysOnlineNetworkMonitor): NetworkMonitor


    @Binds
    fun bindsErrorMonitor(errorMonitor: FakeErrorMonitor): ErrorMonitor
}