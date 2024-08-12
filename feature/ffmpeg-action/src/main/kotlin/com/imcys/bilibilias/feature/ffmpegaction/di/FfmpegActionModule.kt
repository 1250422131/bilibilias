package com.imcys.bilibilias.feature.ffmpegaction.di

import com.imcys.bilibilias.feature.ffmpegaction.DefaultFfmpegActionComponent
import com.imcys.bilibilias.feature.ffmpegaction.FfmpegActionComponent
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface FfmpegActionModule {

    @Binds
    fun componentFactory(impl: DefaultFfmpegActionComponent.Factory): FfmpegActionComponent.Factory
}
