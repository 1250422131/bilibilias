package com.imcys.bilibilias.database.di

import androidx.room.Room
import com.imcys.bilibilias.database.BILIBILIASDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            BILIBILIASDatabase::class.java,
            "bilibilias-database"
        ).build()
    }

    factory {
        get<BILIBILIASDatabase>().biliUsersDao()
    }
    factory {
        get<BILIBILIASDatabase>().biliUserCookiesDao()
    }
}


