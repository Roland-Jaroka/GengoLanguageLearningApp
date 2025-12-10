package com.example.gengolearning.words

import com.example.gengolearning.model.UserSettingsRepository
import com.example.gengolearning.model.Words
import com.example.gengolearning.model.WordsDao
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await

class LanguageWords @Inject constructor(
    private val dao: WordsDao,
    private val userSettingsRepository: UserSettingsRepository
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





}