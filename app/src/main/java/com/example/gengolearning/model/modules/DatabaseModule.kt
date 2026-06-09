package com.example.gengolearning.model.modules

import android.app.Application
import androidx.room.Room
import com.example.gengolearning.data.local.CategoryDatabase
import com.example.gengolearning.data.local.GrammarDao
import com.example.gengolearning.data.local.Migrations
import com.example.gengolearning.data.local.ProfileDao
import com.example.gengolearning.data.local.WordsDao
import com.example.gengolearning.data.local.WordsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): WordsDatabase {
        return Room.databaseBuilder(
            app,
            WordsDatabase::class.java,
            "word_database"
        ).addMigrations(Migrations.MIGRATION_1_2)
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(db: WordsDatabase) : WordsDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun provideGrammarDao(db: WordsDatabase): GrammarDao {
        return db.grammarDao
    }

    @Provides
    @Singleton
    fun provideProfileDao(db: WordsDatabase): ProfileDao {
        return db.profileDao
    }

    @Provides
    @Singleton
    fun provideWordCategoriesDao(db: WordsDatabase): CategoryDatabase {
        return db.categoryDao
    }

}