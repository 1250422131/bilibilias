package com.imcys.bilibilias.core.datastore.di

import androidx.datastore.core.DataStore
import com.imcys.bilibilias.core.datastore.InMemoryDataStore
import com.imcys.bilibilias.core.datastore.UserPreferencesSerializer
import com.imcys.bilibilias.core.datastore.model.UserPreferences
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
