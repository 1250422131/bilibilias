package com.imcys.bilibilias.core.analytics.di

import com.imcys.bilibilias.core.analytics.AnalyticsHelper
import com.imcys.bilibilias.core.analytics.AppCenterAnalyticsHelper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AnalyticsModule {
    @Binds
    abstract fun bindsAnalyticsHelper(analyticsHelperImpl: AppCenterAnalyticsHelper): AnalyticsHelper
}
