package com.imcys.bilibilias.core.datastore.di

import androidx.datastore.core.DataStore
import com.imcys.bilibilias.core.datastore.UserPreferences
import com.imcys.bilibilias.core.datastore.preferences.UserPreferencesSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataStoreModule::class],
)
object TestDataStoreModule {

    @Provides
    @Singleton
    fun providesUserPreferencesDataStore(
        serializer: UserPreferencesSerializer,
    ): DataStore<UserPreferences> = InMemoryDataStore(serializer.defaultValue)
}
