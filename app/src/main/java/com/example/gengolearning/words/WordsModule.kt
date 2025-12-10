package com.example.gengolearning.words

import android.app.Application
import androidx.room.Room
import com.example.gengolearning.model.GrammarDao
import com.example.gengolearning.model.UserSettingsRepository
import com.example.gengolearning.model.WordsDao
import com.example.gengolearning.model.WordsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WordsModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): WordsDatabase {
        return Room.databaseBuilder(
                app,
                WordsDatabase::class.java,
                "word_database"
            ).fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(db: WordsDatabase) : WordsDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun provideGrammarDao(db: WordsDatabase): GrammarDao{
        return db.grammarDao
    }

    @Provides
    @Singleton
    fun provideRepository(dao: WordsDao, userRepo: UserSettingsRepository) : LanguageWords {
        return LanguageWords(dao, userRepo)
    }

    @Provides
    @Singleton
    fun provideUserSettingsRepository(context: Application) : UserSettingsRepository {
        return UserSettingsRepository(context)
    }

}