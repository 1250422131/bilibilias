package com.imcys.bilibilias.feature.ffmpegaction.di

import com.imcys.bilibilias.feature.ffmpegaction.DefaultFfmpegActionComponent
import com.imcys.bilibilias.feature.ffmpegaction.FfmpegActionComponent
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class FfmpegActionModule {

    @Binds
    internal abstract fun componentFactory(impl: DefaultFfmpegActionComponent.Factory): FfmpegActionComponent.Factory
}
