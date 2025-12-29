package com.example.gengolearning.data.repositories


import com.example.gengolearning.data.remote.JishoResponse
import com.example.gengolearning.model.appmodels.Words
import com.example.gengolearning.data.local.WordsDao
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await

class LanguageWords @Inject constructor(
    private val dao: WordsDao,
    userSettingsRepository: UserSettingsRepository,
    private val client: HttpClient
) {




    @OptIn(ExperimentalCoroutinesApi::class)
    val words: Flow<List<Words>> = userSettingsRepository.selectedLanguage.flatMapLatest { language ->
        dao.getAllWords(language.code)
    }







  suspend fun loadWords(language: String){
        val auth= FirebaseAuth.getInstance()
        val uid= auth.currentUser?.uid.toString()
        if (uid.isEmpty()) return

       val result =  Firebase.firestore
            .collection("users")
            .document(uid)
            .collection(language)
            .document(language)
            .collection("words")
            .get().await()

                val list = mutableListOf<Words>()
                for (document in result){
                    val word = document.getString("word") ?: ""
                    val pronunciation = document.getString("pronunciation") ?: ""
                    val translation = document.getString("translation") ?: ""
                    val label = document.getString("label")
                    val id = document.id

                    //Match the firebase words with the local words and get always the
                    //value of the local database for isOnHomePage so it always works locally
                    //Firebase cannot overwrite it when synchronizing
                    val existingWord= words.firstOrNull()?.find { it.id == id }
                    val isOnHomePage= existingWord?.isOnHomePage ?: false



                    list.add(Words(word, pronunciation, translation, id, label, isOnHomePage, language = language))

                }

                dao.upsertWords(list)

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

   suspend fun getWordCount(): Int{
        return dao.getWordCount()
    }

    suspend fun getLanguageCount(): Int{
        return dao.getLanguageCount()
    }


    //Clear on sign out
    suspend fun clearWords() {
        dao.clearWords()
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




}