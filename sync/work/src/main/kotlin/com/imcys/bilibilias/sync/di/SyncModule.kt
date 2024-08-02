package com.imcys.bilibilias.sync.di

import com.imcys.bilibilias.core.data.util.SyncManager
import com.imcys.bilibilias.sync.status.StubSyncSubscriber
import com.imcys.bilibilias.sync.status.SyncSubscriber
import com.imcys.bilibilias.sync.status.WorkManagerSyncManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SyncModule {
    @Binds
    internal abstract fun bindsSyncStatusMonitor(
        syncStatusMonitor: WorkManagerSyncManager,
    ): SyncManager

    @Binds
    internal abstract fun bindsSyncSubscriber(
        syncSubscriber: StubSyncSubscriber,
    ): SyncSubscriber
}
