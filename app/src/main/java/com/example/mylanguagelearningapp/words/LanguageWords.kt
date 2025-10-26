package com.example.mylanguagelearningapp.words

import androidx.compose.runtime.mutableStateListOf
import com.example.mylanguagelearningapp.model.Words
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

object LanguageWords {
    private val _words = MutableStateFlow<List<Words>>(emptyList())
    val words: StateFlow<List<Words>> = _words

    private val languageCatch= mutableMapOf<String,List<Words>>()





    fun loadWords(language: String){
        val auth= FirebaseAuth.getInstance()
        val uid= auth.currentUser?.uid.toString()
        if (uid.isEmpty()) return

        _words.value = languageCatch[language] ?: emptyList()

        Firebase.firestore
            .collection("users")
            .document(uid)
            .collection(language)
            .document(language)
            .collection("words")
            .get().addOnSuccessListener {
                val list = mutableListOf<Words>()
                for (document in it){
                    val word = document.getString("word") ?: ""
                    val pronunciation = document.getString("pronunciation") ?: ""
                    val translation = document.getString("translation") ?: ""
                    val label = document.getString("label")
                    val isOnHomePage = document.getBoolean("isOnHomePage") ?: false
                    val id = document.id
                    list.add(Words(word, pronunciation, translation, id, label, isOnHomePage))

                }
                languageCatch[language] = list
                _words.value = list
                println("WordsLoaded")

            }

    }

    fun addWord(word: Words, language: String){
        val auth= FirebaseAuth.getInstance()
        val uid= auth.currentUser?.uid.toString()
        if (uid.isEmpty()) return

        val docID= UUID.randomUUID().toString()
        val newWord = word.copy(id= docID)

        _words.value = _words.value + newWord

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

        _words.value = _words.value.filterNot { it.id == id }

        Firebase.firestore.collection("users")
            .document(uid)
            .collection(language)
            .document(language)
            .collection("words")
            .document(id)
            .delete()

    }
}