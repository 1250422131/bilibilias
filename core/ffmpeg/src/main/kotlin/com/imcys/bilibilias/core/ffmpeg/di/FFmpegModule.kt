package com.imcys.bilibilias.core.ffmpeg.di

import com.imcys.bilibilias.core.ffmpeg.FFmpegKitImpl
import com.imcys.bilibilias.core.ffmpeg.IFFmpegWork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FFmpegModule {
    @Binds
    abstract fun bindIFFmepgWork(fFmpegKitImpl: FFmpegKitImpl): IFFmpegWork
}
