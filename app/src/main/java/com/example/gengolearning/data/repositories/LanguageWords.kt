package com.example.gengolearning.data.repositories


import android.util.Log
import com.example.gengolearning.data.local.CategoryDatabase
import com.example.gengolearning.data.local.WordsDao
import com.example.gengolearning.data.remote.JishoResponse
import com.example.gengolearning.model.api_call_interfaces.NetworkInterface
import com.example.gengolearning.model.appmodels.DictionaryRequest
import com.example.gengolearning.model.appmodels.NewsResponse
import com.example.gengolearning.model.appmodels.QuizRequest
import com.example.gengolearning.model.appmodels.WordCategories
import com.example.gengolearning.model.appmodels.Words
import com.example.gengolearning.model.errors.NetworkError
import com.example.gengolearning.model.results.CloudSyncResults
import com.example.gengolearning.model.results.Response
import com.example.gengolearning.model.tokens.TokenProvider
import com.example.gengolearning.ui.features.dashboard.home.aiquiz.AiQuiz
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import okio.IOException
import retrofit2.HttpException

class LanguageWords @Inject constructor(
    private val dao: WordsDao,
    private val userSettingsRepository: UserSettingsRepository,
    private val client: HttpClient,
    private val categoriesDao: CategoryDatabase,
    private val tokenProvider: TokenProvider,
    private val api: NetworkInterface
) {




    @OptIn(ExperimentalCoroutinesApi::class)
    val words: Flow<List<Words>> = userSettingsRepository.selectedLanguage.flatMapLatest { language ->
        dao.getAllWords(language.code)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val categories: Flow<List<WordCategories>> = userSettingsRepository.selectedLanguage.flatMapLatest { language ->
        categoriesDao.getAllCategories(language.code)
    }

    val cloudSyncWasSuccess = MutableSharedFlow<CloudSyncResults>(replay = 1)







    @OptIn(ExperimentalCoroutinesApi::class)
    fun getHomePageWord(): Flow<List<Words>> {
      return userSettingsRepository.selectedLanguage.flatMapLatest { language ->
           dao.getHomePageWords(language.code)
       }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAllWords(): Flow<List<Words>> {
        return userSettingsRepository.selectedLanguage.flatMapLatest { language ->
            dao.getAllWords(language.code)
        }
    }


  suspend fun loadWords(language: String, forceServerLoad: Boolean = false){
        val auth= FirebaseAuth.getInstance()
        val uid= auth.currentUser?.uid.toString()
        if (uid.isEmpty()) return

      //By Setting the Source.Server it will make sure to try to fetch from server only, it is needed
      //upon Login to force the user to sync the users data from cloud first
       val result =  Firebase.firestore
            .collection("users")
            .document(uid)
            .collection(language)
            .document(language)
            .collection("words")
            .get(
                if (forceServerLoad) Source.SERVER else Source.DEFAULT
            ).await()

                val list = mutableListOf<Words>()
                for (document in result){
                    val word = document.getString("word") ?: ""
                    val pronunciation = document.getString("pronunciation") ?: ""
                    val translation = document.getString("translation") ?: ""
                    val id = document.id
                    val categories = document.get("categories") as? List<String> ?: emptyList()

                    //Match the firebase words with the local words and get always the
                    //value of the local database for isOnHomePage so it always works locally
                    //Firebase cannot overwrite it when synchronizing
                    val existingWord= words.firstOrNull()?.find { it.id == id }
                    val isOnHomePage= existingWord?.isOnHomePage ?: false



                    list.add(Words(word, pronunciation, translation, id,  isOnHomePage, language = language, category = categories))

                }

      if (result.metadata.isFromCache) {
          Log.d("Sync", "Emmit a value")

          cloudSyncWasSuccess.emit(CloudSyncResults.Failure)

      } else {

          cloudSyncWasSuccess.emit(CloudSyncResults.Success)


          dao.upsertWords(list)
      }


    }

    suspend fun loadCategories(language: String) {
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid.toString()
        if (uid.isEmpty()) return

        val result = Firebase.firestore
            .collection("users")
            .document(uid)
            .collection(language)
            .document(language)
            .collection("categories")
            .get().await()

        val categoryList = mutableListOf<WordCategories>()
        for (document in result) {
            val id = document.id
            val categoryName = document.getString("categoryName") ?: ""
            val color = document.getLong("color")
            val language = document.getString("language") ?: ""

            categoryList.add(
                WordCategories(
                    id,
                    categoryName,
                    color?.toInt(),
                    language
                )
            )
        }
        categoriesDao.upsertCategories(categoryList)
    }

   suspend fun addWord(word: Words, language: String){
        val auth= FirebaseAuth.getInstance()
        val uid= auth.currentUser?.uid.toString()
        if (uid.isEmpty()) return

        val docID= word.id

        dao.updateWords(word)

        Firebase.firestore
            .collection("users")
            .document(uid)
            .collection(language)
            .document(language)
            .collection("words")
            .document(docID)
            .set(
                mapOf(
                    "word" to word.word,
                    "pronunciation" to word.pronunciation,
                    "translation" to word.translation
                )
            )
    }


    suspend fun addCategoryToFirebase(category: WordCategories, language: String) {
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid.toString()
        if (uid.isEmpty()) return

        Firebase.firestore
            .collection("users")
            .document(uid)
            .collection(language)
            .document(language)
            .collection("categories")
            .document(category.id)
            .set(
                mapOf(
                    "categoryName" to category.categoryName,
                    "color" to category.color,
                    "language" to language
                )
            )
    }

    suspend fun onRemove(id: String, language: String){
        val auth= FirebaseAuth.getInstance()
        val uid= auth.currentUser?.uid.toString()
        if (uid.isEmpty()) return

        dao.deleteWords(Words(id = id))


        Firebase.firestore.collection("users")
            .document(uid)
            .collection(language)
            .document(language)
            .collection("words")
            .document(id)
            .delete()

    }

    suspend fun removeCategoryFromFirebase(id: String, language: String){

        val auth= FirebaseAuth.getInstance()
        val uid= auth.currentUser?.uid.toString()
        if (uid.isEmpty()) return

        Firebase.firestore
            .collection("users")
            .document(uid)
            .collection(language)
            .document(language)
            .collection("categories")
            .document(id)
            .delete()
    }

    suspend fun onHomePage(id: String, isOnHomePage: Boolean){
       dao.updateIsOnHomePage(id, isOnHomePage)

    }

    suspend fun updateWord(id: String, word: String, translation: String, pronunciation: String, language: String, words: Words?){
        if (words == null) return

        dao.updateWord(words.copy(word = word, translation = translation, pronunciation = pronunciation))

        val auth= FirebaseAuth.getInstance()
        val uid= auth.currentUser?.uid.toString()
        if (uid.isEmpty()) return

        Firebase.firestore
            .collection("users")
            .document(uid)
            .collection(language)
            .document(language)
            .collection("words")
            .document(id)
            .update(
                mapOf(
                    "word" to word,
                    "pronunciation" to pronunciation,
                    "translation" to translation
                )
            )

    }

    suspend fun updateWordWithCategoryOnFirebase(word: Words, language: String) {
        val auth= FirebaseAuth.getInstance()
        val uid= auth.currentUser?.uid.toString()
        if (uid.isEmpty()) return
        Firebase.firestore
            .collection("users")
            .document(uid)
            .collection(language)
            .document(language)
            .collection("words")
            .document(word.id)
            .update(
                mapOf(
                    "categories" to word.category
                )
            )
    }

    suspend fun updateCategoryOnFirebase(category: WordCategories, language: String) {
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid.toString()
        if (uid.isEmpty()) return

        Firebase.firestore
            .collection("users")
            .document(uid)
            .collection(language)
            .document(language)
            .collection("categories")
            .document(category.id)
            .update(
                mapOf(
                    "categoryName" to category.categoryName,
                    "color" to category.color
                )
            )
    }

    suspend fun updateLocalWord(words: Words) {
        dao.updateWords(words)
    }

    suspend fun updateWordWithCategory(word: Words) {
        dao.updateWords(word)
    }



    fun getWordCount(): Flow<Int>{
        return dao.getWordCount()
    }

   fun getLanguageCount(): Flow<Int>{
        return dao.getLanguageCount()
    }

    suspend fun deleteCategory(category: WordCategories){
        categoriesDao.deleteCategory(category)
    }



    //Clear on sign out
    suspend fun clearWords() {
        dao.clearWords()
    }

    suspend fun addCategory(category: WordCategories){
        categoriesDao.upsertCategory(category)
    }

    suspend fun clearCategories(){
        categoriesDao.clearCategories()
    }







    suspend fun getWordsFromApi(searchKey: String): Response<List<Words>, NetworkError.BasicNetworkError> {
        //Get the words from Jisho API
        //sorted by is common cause it is the same in Jisho web
        return try {

            val response: JishoResponse = api.getJishoWords(
                request = DictionaryRequest(
                    word = searchKey
                ))

     val data =  response.data.sortedByDescending{ it.isCommon }.flatMap { entry ->

            //Gets the first element of the Jisho API Japanese cause there are words which has
            //Different forms
            val firstForm = entry.japanese.firstOrNull() ?: return@flatMap emptyList()

            //Makes a list of it cause flatMap{} needs a list cause it returns a list of list
            listOf(
                Words(
                    word= firstForm.word,
                    pronunciation = firstForm.reading,
                    translation = entry.senses.firstOrNull()?.englishDefinitions?.joinToString() ?: ""
                )
            )
        }

         Response.Success(data)

        } catch (e: IOException) {
            Response.Error(error = NetworkError.BasicNetworkError.NO_INTERNET)
        } catch (e: HttpException){
            Response.Error(NetworkError.BasicNetworkError.SERVER_DOWN)
        } catch (e: Exception) {
            Response.Error(NetworkError.BasicNetworkError.UNKOWN_ERROR)
        }


    }

    suspend fun getNews(): List<NewsResponse> {

        return api.getNews()
    }

    suspend fun getAiQuiz(language: String, level: String): Response<List<AiQuiz>, NetworkError.GeminaiNetworkError> {
        return try {
            val token = tokenProvider.getToken()

            val response = api.getGeminiQuiz(token = "Bearer $token",
                request = QuizRequest(
                language, level
            ))

            Response.Success(response)
        } catch (e: HttpException) {
            when (e.code()){
                429 -> {
                    if(e.message?.contains("you exceeded your current quota", ignoreCase = true) ?: false) {
                        Response.Error(NetworkError.GeminaiNetworkError.RATE_LIMIT_REACHED)} else {
                        Response.Error(NetworkError.GeminaiNetworkError.HEAVY_SERVERS)
                    }
                }

                in 500..599 -> {
                    Response.Error(NetworkError.GeminaiNetworkError.HEAVY_SERVERS)
                }

                else -> {
                    Response.Error(NetworkError.GeminaiNetworkError.UNKOWN_ERROR)
                }
            }
        } catch (e: IOException) {
            Response.Error(NetworkError.GeminaiNetworkError.NO_INTERNET)
        }

        catch (e: Exception) {
            Response.Error(NetworkError.GeminaiNetworkError.UNKOWN_ERROR)
        }
    }

    fun uploadQuizToFirebase(quizList: List<AiQuiz>, language: String, level: String) {
        val auth = FirebaseAuth.getInstance()
        val db = Firebase.firestore

        val batch = db.batch()
        quizList.forEach { quiz ->
            val docRef = db.collection("aiQuiz")
                .document(language)
                .collection("levels")
                .document(level)
                .collection("questions")
                .document()

            batch.set(
                docRef,
                mapOf(
                    "question" to quiz.question,
                    "option" to quiz.options,
                    "correctAnswer" to quiz.correctAnswer
                )
            )
        }

        batch.commit()
            .addOnSuccessListener {
                Log.d("Firestore", "QuizUploaded")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Upload failed", e)
            }

    }

    fun getFirebaseToken(onResult: (String?) -> Unit ){
        FirebaseAuth.getInstance().currentUser
            ?.getIdToken(true)
            ?.addOnSuccessListener { result ->
                onResult(result.token)
                Log.d("Token", result.token.toString())
            }
    }


    suspend fun testAPI(token: String?) {
       val response = client.get("https://gengolearningbackend.jaroka-roland.workers.dev/test") {
           header("Authorization", "Bearer $token")
           contentType(ContentType.Application.Json)
       }

        print("API status: ${response.bodyAsText()}")
        Log.d("API response", response.bodyAsText())
    }
}