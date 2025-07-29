package com.imcys.bilibilias.core.data

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val UseCaseModule = module {
    singleOf(::GetCachedEpisodeStateUseCase)
    singleOf(::GetEpisodeInfoUseCase)
    singleOf(::MediaSourceSelectedUseCase)
}