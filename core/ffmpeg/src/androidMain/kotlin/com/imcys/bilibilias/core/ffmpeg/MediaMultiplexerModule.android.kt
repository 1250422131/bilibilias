package com.imcys.bilibilias.core.ffmpeg

import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val MediaMultiplexerModule: Module = module {
    singleOf(::Media3MediaMultiplexer) {
        named("default")
        bind<MediaMultiplexer>()
    }
    singleOf(::FfmpegMediaMultiplexer) {
        named("ffmpeg")
        bind<MediaMultiplexer>()
    }
}