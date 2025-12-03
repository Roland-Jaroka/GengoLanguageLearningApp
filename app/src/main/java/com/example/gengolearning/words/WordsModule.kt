package com.example.gengolearning.words

import android.app.Application
import androidx.room.Room
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
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(db: WordsDatabase) : WordsDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun provideRepository(dao: WordsDao) : LanguageWords {
        return LanguageWords(dao)
    }

    @Provides
    @Singleton
    fun provideUserSettingsRepository(context: Application, languageWords: LanguageWords) : UserSettingsRepository {
        return UserSettingsRepository(context, languageWords)
    }

}