package com.example.gengolearning.model.modules

import android.content.Context
import com.example.gengolearning.model.tokens.FirebaseTokenProvider
import com.example.gengolearning.model.tokens.TokenProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WordsModule {




    @Provides
    @Singleton
    fun provideTokenProvider(): TokenProvider {
        return FirebaseTokenProvider()
    }






    @Provides
    @Singleton
    fun provideHttpsClient(
        @ApplicationContext context: Context
    ) : HttpClient {
        return HttpClient(OkHttp) {

            expectSuccess = true

//            HttpClientConfig.install(ContentNegotiation) {
//                json(Json {
//                    ignoreUnknownKeys = true
//                })
//            }
//
//            engine {
//                addInterceptor(ChuckerInterceptor(context))
//            }
//
//            HttpClientConfig.install(Logging) {
//                LoggingConfig.logger = Logger.Companion.SIMPLE
//                LoggingConfig.level = LogLevel.ALL
//            }
//
//            HttpClientConfig.install(HttpTimeout) {
//                HttpTimeoutConfig.requestTimeoutMillis = 30_000
//            }




            defaultRequest {
                header(HttpHeaders.Accept, ContentType.Application.Json)
            }
        }


    }

}