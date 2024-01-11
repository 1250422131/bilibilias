package com.imcys.network.repository.video

import com.imcys.network.di.NetworkModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
internal annotation class Mock

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
class MockModule {
    @Provides
    @Singleton
    fun provideHttpClientEngine(): HttpClientEngine = MockEngine(MockEngineConfig())
}
