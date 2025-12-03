package com.example.gengolearning.words

import com.example.gengolearning.model.Words
import com.example.gengolearning.model.WordsDao
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class LanguageWords @Inject constructor(private val dao: WordsDao) {
//    private val _words = MutableStateFlow<List<Words>>(emptyList())
//    val words: StateFlow<List<Words>> = _words

    val words: Flow<List<Words>> = dao.getAllWords()




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
                    val isOnHomePage = document.getBoolean("isOnHomePage") ?: false
                    val id = document.id
                    list.add(Words(word, pronunciation, translation, id, label, isOnHomePage))

                }

                dao.upsertWords(list)

    }

    fun addWord(word: Words, language: String){
        val auth= FirebaseAuth.getInstance()
        val uid= auth.currentUser?.uid.toString()
        if (uid.isEmpty()) return

        val docID= UUID.randomUUID().toString()
        val newWord = word.copy(id= docID)

//        _words.value = _words.value + newWord

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

    fun onRemove(id: String, language: String){
        val auth= FirebaseAuth.getInstance()
        val uid= auth.currentUser?.uid.toString()
        if (uid.isEmpty()) return

//        _words.value = _words.value.filterNot { it.id == id }

        Firebase.firestore.collection("users")
            .document(uid)
            .collection(language)
            .document(language)
            .collection("words")
            .document(id)
            .delete()

    }

    fun onHomePage(id: String, language: String, isOnHomePage: Boolean){
        val auth= FirebaseAuth.getInstance()
        val uid= auth.currentUser?.uid.toString()
        if (uid.isEmpty()) return

//            _words.value = _words.value.map{ it ->
//                if (it.id== id){
//                    it.copy(isOnHomePage = isOnHomePage)
//                } else {
//                    it
//                }
//            }

        Firebase.firestore.collection("users")
            .document(uid)
            .collection(language)
            .document(language)
            .collection("words")
            .document(id)
            .update("isOnHomePage", isOnHomePage)
    }
}