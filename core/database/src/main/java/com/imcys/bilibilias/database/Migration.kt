package com.imcys.bilibilias.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE download_segment ADD COLUMN platform_unique_id TEXT NOT NULL DEFAULT ''")
    }
}
