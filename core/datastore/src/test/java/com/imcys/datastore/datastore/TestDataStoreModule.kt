package com.imcys.datastore.datastore

import android.content.*
import androidx.datastore.core.*
import com.bilias.core.datastore.cookie.*
import com.imcys.common.di.*
import com.imcys.datastore.di.*
import dagger.*
import dagger.hilt.android.qualifiers.*
import dagger.hilt.components.*
import dagger.hilt.testing.*
import kotlinx.coroutines.*
import org.junit.rules.*
import javax.inject.*

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataStoreModule::class],
)
internal object TestDataStoreModule {

    @Provides
    @Singleton
    fun providesUserCookieDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(AsDispatchers.IO) ioDispatcher: CoroutineDispatcher,
        @AppCoroutineScope scope: CoroutineScope,
        cookieStorageSerializer: CookieStorageSerializer,
        tmpFolder: TemporaryFolder,
    ): DataStore<CookieStorage> =
        tmpFolder.testUserPreferencesDataStore(
            coroutineScope = scope,
            cookieStorageSerializer = cookieStorageSerializer,
        )
}
fun TemporaryFolder.testUserPreferencesDataStore(
    coroutineScope: CoroutineScope,
    cookieStorageSerializer: CookieStorageSerializer = CookieStorageSerializer(),
) = DataStoreFactory.create(
    serializer = cookieStorageSerializer,
    scope = coroutineScope,
) {
    newFile("user_cookie_test.pb")
}