package com.imcys.bilibilias.database.di

import androidx.room.Room
import com.imcys.bilibilias.database.BILIBILIASDatabase
import com.imcys.bilibilias.database.MIGRATION_1_2
import com.imcys.bilibilias.database.MIGRATION_2_3
import com.imcys.bilibilias.database.MIGRATION_3_4
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            BILIBILIASDatabase::class.java,
            "bilibilias-database"
        ).addMigrations(MIGRATION_1_2,MIGRATION_2_3,MIGRATION_3_4).build()
    }

    factory {
        get<BILIBILIASDatabase>().biliUsersDao()
    }
    factory {
        get<BILIBILIASDatabase>().biliUserCookiesDao()
    }
    factory {
        get<BILIBILIASDatabase>().downloadTaskDao()
    }
}


