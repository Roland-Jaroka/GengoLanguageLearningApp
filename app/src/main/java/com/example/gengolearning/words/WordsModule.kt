package com.example.gengolearning.words

import android.app.Application
import androidx.room.Room
import com.example.gengolearning.data.local.CategoryDatabase
import com.example.gengolearning.data.local.GrammarDao
import com.example.gengolearning.data.local.Migrations
import com.example.gengolearning.data.local.ProfileDao
import com.example.gengolearning.data.local.WordsDao
import com.example.gengolearning.data.local.WordsDatabase
import com.example.gengolearning.data.repositories.LanguageWords
import com.example.gengolearning.data.repositories.UserSettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
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
    fun provideGrammarDao(db: WordsDatabase): GrammarDao{
        return db.grammarDao
    }

    @Provides
    @Singleton
    fun provideProfileDao(db: WordsDatabase): ProfileDao{
        return db.profileDao
    }

    @Provides
    @Singleton
    fun provideWordCategoriesDao(db: WordsDatabase): CategoryDatabase {
        return db.categoryDao
    }


    @Provides
    @Singleton
    fun provideRepository(dao: WordsDao, userRepo: UserSettingsRepository, client: HttpClient, categoriesDao: CategoryDatabase) : LanguageWords {
        return LanguageWords(dao, userRepo, client, categoriesDao)
    }

    @Provides
    @Singleton
    fun provideUserSettingsRepository(context: Application, profileDao: ProfileDao) : UserSettingsRepository {
        return UserSettingsRepository(context, profileDao)
    }






    @Provides
    @Singleton
    fun provideHttpsClient() : HttpClient {
        return HttpClient(CIO){

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }

//            install(Logging){
//                logger = Logger.SIMPLE
//                level= LogLevel.ALL
//            }

            install(HttpTimeout) {
                requestTimeoutMillis = 10_000
            }




            defaultRequest {
                header(HttpHeaders.Accept, ContentType.Application.Json)
            }
        }


    }

}