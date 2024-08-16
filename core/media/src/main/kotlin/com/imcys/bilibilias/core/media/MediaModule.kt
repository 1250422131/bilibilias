package com.imcys.bilibilias.core.media

import com.imcys.bilibilias.core.media.services.LocalMediaService
import com.imcys.bilibilias.core.media.services.MediaService
import com.imcys.bilibilias.core.media.sync.LocalMediaInfoSynchronizer
import com.imcys.bilibilias.core.media.sync.LocalMediaSynchronizer
import com.imcys.bilibilias.core.media.sync.MediaInfoSynchronizer
import com.imcys.bilibilias.core.media.sync.MediaSynchronizer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface MediaModule {

    @Binds
    @Singleton
    fun bindsMediaSynchronizer(
        mediaSynchronizer: LocalMediaSynchronizer,
    ): MediaSynchronizer

    @Binds
    @Singleton
    fun bindsMediaInfoSynchronizer(
        mediaInfoSynchronizer: LocalMediaInfoSynchronizer,
    ): MediaInfoSynchronizer

    @Binds
    @Singleton
    fun bindMediaService(
        mediaService: LocalMediaService,
    ): MediaService
}
