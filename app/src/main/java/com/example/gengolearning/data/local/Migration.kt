package com.example.gengolearning.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migrations {

    val MIGRATION_1_2 = object : Migration(5,7) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                    CREATE TABLE wordcategories_new (
                       id TEXT NOT NULL,
                       categoryName TEXT NOT NULL,
                       color INTEGER,
                       language TEXT NOT NULL,
                       PRIMARY KEY(id) 
                       )
                       """
                    )
            database.execSQL("""
                INSERT INTO wordcategories_new (id, categoryName, color, language)
                SELECT CAST (id AS TEXT), categoryName, color, language FROM wordcategories
                """
            )

            database.execSQL("DROP TABLE wordcategories")
            database.execSQL("ALTER TABLE wordcategories_new RENAME TO wordcategories")

        }
        
        
    }
}