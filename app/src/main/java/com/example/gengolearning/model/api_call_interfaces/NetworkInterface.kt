package com.example.gengolearning.model.api_call_interfaces

import com.example.gengolearning.data.remote.JishoResponse
import com.example.gengolearning.model.appmodels.DictionaryRequest
import com.example.gengolearning.model.appmodels.GeminaiGrammarRequest
import com.example.gengolearning.model.appmodels.GeminaiGrammarResponse
import com.example.gengolearning.model.appmodels.NewsResponse
import com.example.gengolearning.model.appmodels.QuizRequest
import com.example.gengolearning.ui.features.dashboard.home.aiquiz.AiQuiz
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface NetworkInterface {
  @GET("/news")
  suspend fun getNews(): List<NewsResponse>

  @POST("/dictionary")
  suspend fun getJishoWords(
      @Body request: DictionaryRequest
  ): JishoResponse

  @POST("/geminai/languageexam")
  suspend fun getGeminiQuiz(
      @Header("Authorization") token: String?,
      @Body request: QuizRequest
  ): List<AiQuiz>

  @POST("/geminai/grammar")
  suspend fun getGrammar(
      @Header("App-Language") appLanguage: String,
      @Body request: GeminaiGrammarRequest
  ): GeminaiGrammarResponse
}