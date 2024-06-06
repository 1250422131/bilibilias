package com.imbys.bilibilias.feature.authorspace.di

import com.imbys.bilibilias.feature.authorspace.AuthorSpaceComponent
import com.imbys.bilibilias.feature.authorspace.DefaultAuthorSpaceComponent
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AuthorSpaceModule {

    @Binds
    fun componentFactory(impl: DefaultAuthorSpaceComponent.Factory): AuthorSpaceComponent.Factory
}
