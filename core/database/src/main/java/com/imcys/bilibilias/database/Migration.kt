package com.imcys.bilibilias.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE download_segment ADD COLUMN platform_unique_id TEXT NOT NULL DEFAULT ''")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE download_segment ADD COLUMN naming_convention_info TEXT")
    }
}
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE download_segment ADD COLUMN media_container TEXT NOT NULL DEFAULT ''")
        db.execSQL("ALTER TABLE download_segment ADD COLUMN quality_description TEXT")
        db.execSQL(
            """
                UPDATE download_segment
              SET media_container = CASE download_mode
                                      WHEN 'AUDIO_VIDEO' THEN 'mp4'
                                      WHEN 'AUDIO_ONLY'  THEN 'mp3'
                                      WHEN 'VIDEO_ONLY'  THEN 'mp4'
                                      ELSE 'mp4'
                                    END
            """.trimIndent()
        )
    }
}