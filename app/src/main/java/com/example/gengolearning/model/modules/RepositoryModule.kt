package com.example.gengolearning.model.modules

import android.app.Application
import com.example.gengolearning.data.local.CategoryDatabase
import com.example.gengolearning.data.local.ProfileDao
import com.example.gengolearning.data.local.WordsDao
import com.example.gengolearning.data.repositories.LanguageWords
import com.example.gengolearning.data.repositories.UserSettingsRepository
import com.example.gengolearning.model.api_call_interfaces.NetworkInterface
import com.example.gengolearning.model.tokens.TokenProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(dao: WordsDao, userRepo: UserSettingsRepository, client: HttpClient, categoriesDao: CategoryDatabase, tokenProvider: TokenProvider, api: NetworkInterface) : LanguageWords {
        return LanguageWords(dao, userRepo, client, categoriesDao, tokenProvider, api)
    }

    @Provides
    @Singleton
    fun provideUserSettingsRepository(context: Application, profileDao: ProfileDao) : UserSettingsRepository {
        return UserSettingsRepository(context, profileDao)
    }

}