package com.imcys.bilibilias.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.imcys.bilibilias.core.common.network.AsDispatchers
import com.imcys.bilibilias.core.common.network.Dispatcher
import com.imcys.bilibilias.core.common.network.di.ApplicationScope
import com.imcys.bilibilias.core.datastore.AsCookieStoreSerializer
import com.imcys.bilibilias.core.datastore.UserPreferencesSerializer
import com.imcys.bilibilias.core.datastore.UsersSerializer
import com.imcys.bilibilias.core.datastore.model.AsCookieStore
import com.imcys.bilibilias.core.datastore.model.UserPreferences
import com.imcys.bilibilias.core.datastore.model.Users
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.protobuf.ProtoBuf
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {
    @Provides
    @Singleton
    internal fun providesAsCookieStoreDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(AsDispatchers.IO) ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
        asCookieStoreSerializer: AsCookieStoreSerializer,
    ): DataStore<AsCookieStore> =
        DataStoreFactory.create(
            serializer = asCookieStoreSerializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
        ) {
            context.dataStoreFile("as_cookie.pb")
        }

    @Provides
    @Singleton
    internal fun providesUsersDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(AsDispatchers.IO) ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
        usersSerializer: UsersSerializer,
    ): DataStore<Users> =
        DataStoreFactory.create(
            serializer = usersSerializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
        ) {
            context.dataStoreFile("users.pb")
        }

    @Provides
    @Singleton
    internal fun providesUserPreferencesDataStor1e(
        @ApplicationContext context: Context,
        @Dispatcher(AsDispatchers.IO) ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
        userPreferencesSerializer: UserPreferencesSerializer,
    ): DataStore<UserPreferences> =
        DataStoreFactory.create(
            serializer = userPreferencesSerializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
        ) {
            context.dataStoreFile("user_preferences.pb")
        }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    internal fun providesProtobuf() = ProtoBuf { }
}
