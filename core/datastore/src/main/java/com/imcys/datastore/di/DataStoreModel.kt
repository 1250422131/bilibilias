package com.imcys.datastore.di

import com.imcys.datastore.datastore.CookieDataSource
import com.imcys.datastore.datastore.ICookieDataSource
import com.imcys.datastore.fastkv.CookiesData
import com.imcys.datastore.fastkv.ICookieStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataStoreModel {
    @Binds
    fun bindICookieStore(cookiesData: CookiesData): ICookieStore

   /* @Provides
    @Singleton
    fun providesUserCookieSerializerDataStore(
        @ApplicationContext context: Context,
        @AppCoroutineScope scope: CoroutineScope,
        userCookieSerializer: UserCookieSerializer,
    ): DataStore<UserCookie> =
        DataStoreFactory.create(
            serializer = userCookieSerializer,
            scope = CoroutineScope(scope.coroutineContext),
        ) {
            context.dataStoreFile("user_cookie.pb")
        }*/

    @Binds
    fun bindICookieDataSource(cookieDataSource: CookieDataSource): ICookieDataSource
}
