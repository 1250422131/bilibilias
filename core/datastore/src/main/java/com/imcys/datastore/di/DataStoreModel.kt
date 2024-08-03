package com.imcys.datastore.di

import android.content.*
import androidx.datastore.*
import androidx.datastore.core.*
import com.bilias.core.datastore.cookie.*
import com.imcys.common.di.*
import com.imcys.datastore.datastore.*
import dagger.*
import dagger.hilt.*
import dagger.hilt.android.qualifiers.*
import dagger.hilt.components.*
import kotlinx.coroutines.*
import javax.inject.*

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    internal fun providesUserCookieDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(AsDispatchers.IO) ioDispatcher: CoroutineDispatcher,
        @AppCoroutineScope scope: CoroutineScope,
        cookieStorageSerializer: CookieStorageSerializer
    ): DataStore<CookieStorage> =
        DataStoreFactory.create(
            serializer = cookieStorageSerializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
        ) {
            context.dataStoreFile("user_cookie.pb")
        }
}
