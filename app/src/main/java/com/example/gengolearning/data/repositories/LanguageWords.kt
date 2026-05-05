package com.example.gengolearning.data.repositories


import android.util.Log
import com.example.gengolearning.data.local.CategoryDatabase
import com.example.gengolearning.data.local.WordsDao
import com.example.gengolearning.data.remote.JishoResponse
import com.example.gengolearning.model.appmodels.NewsResponse
import com.example.gengolearning.model.appmodels.WordCategories
import com.example.gengolearning.model.appmodels.Words
import com.example.gengolearning.ui.features.dashboard.home.aiquiz.AiQuiz
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json

class LanguageWords @Inject constructor(
    private val dao: WordsDao,
    userSettingsRepository: UserSettingsRepository,
    private val client: HttpClient,
    private val categoriesDao: CategoryDatabase
) {




    @OptIn(ExperimentalCoroutinesApi::class)
    val words: Flow<List<Words>> = userSettingsRepository.selectedLanguage.flatMapLatest { language ->
        dao.getAllWords(language.code)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val categories: Flow<List<WordCategories>> = userSettingsRepository.selectedLanguage.flatMapLatest { language ->
        categoriesDao.getAllCategories(language.code)
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

                dao.upsertWords(list)

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







    suspend fun getWordsFromApi(searchKey: String): List<Words> {


        //Get the words of my own repository Json file
//      val jsonText= client.get("https://raw.githubusercontent.com/Roland-Jaroka/LanguageWordsApi/refs/heads/main/japanese_words_repository.json")
//            .bodyAsText()
//
//        return Json {ignoreUnknownKeys = true}.decodeFromString(jsonText)


        //Get the words from Jisho API
        val response: JishoResponse = client.get("https://jisho.org/api/v1/search/words?keyword=$searchKey")
            .body()


        //sorted by is common cause it is the same in Jisho web
        return response.data.sortedByDescending{ it.isCommon }.flatMap { entry ->

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


    }

    suspend fun getNews(): List<NewsResponse> {
        val response = client.get("https://raw.githubusercontent.com/Roland-Jaroka/LanguageWordsApi/refs/heads/main/News.json")
            .bodyAsText()



        return Json { ignoreUnknownKeys = true }.decodeFromString(response)
    }


    suspend fun getAiquiz(language: String, level: String): List<AiQuiz> {
        val model = Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel("gemini-2.5-flash")
        val prompt = """
             You are a JSON generator.

             Return ONLY valid JSON.
             No markdown, no explanations, no extra text.

             Generate exactly 5 $level reading comprehension questions.

             Each item must follow this structure:

             {
               "question": "string",
               "options": ["string", "string", "string", "string"],
               "correctAnswer": "string"
             }

             Rules:
             - question must be a medium-length $language reading passage + question
             - $level level difficulty
             - 4 answer options exactly
             - only one correct answer
             - correctAnswer must match one option exactly
             - all content must be in natural $language

             Return ONLY a JSON array.
        """.trimIndent()

        val response = model.generateContent(prompt)
        Log.d("Ai_Quiz", prompt)
        Log.d("Ai_Quiz", response.text ?: "")

        return  Json { ignoreUnknownKeys = true }.decodeFromString<List<AiQuiz>>(response.text ?: "")
    }




}