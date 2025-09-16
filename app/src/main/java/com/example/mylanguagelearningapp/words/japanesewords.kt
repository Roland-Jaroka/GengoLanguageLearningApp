package com.example.mylanguagelearningapp.words

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.example.mylanguagelearningapp.model.Words
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await


object JapaneseWords {
    val wordList = mutableStateListOf<Words>()



    val auth= FirebaseAuth.getInstance()

    var error = ""


    fun addWord(word: Words){

        val uid= auth.currentUser?.uid.toString()
        if (uid.isEmpty()) return

        Firebase.firestore
            .collection("users")
            .document(uid)
            .collection("words")
            .add(
                mapOf(
                    "word" to word.word,
                    "pronunciation" to word.pronunciation,
                    "translation" to word.translation
                )
            ).addOnSuccessListener {
                println("Word added successfully")
            }
            .addOnFailureListener {
                println("Error adding word")
                error = it.message.toString()

            }
    }
  fun loadWords() {
        val uid= auth.currentUser?.uid.toString()
        if (uid.isEmpty()) return

        Firebase.firestore
            .collection("users")
            .document(uid)
            .collection("words")
            .get().addOnSuccessListener {
                wordList.clear()

                for (document in it) {
                    val word = document.getString("word") ?: ""
                    val pronunciation = document.getString("pronunciation") ?: ""
                    val translation = document.getString("translation") ?: ""
                    val id = document.id
                    wordList.add(Words(word, pronunciation, translation, id))
                }

                Log.d("JapaneseWords", "Loaded ${wordList.size} words")
                Log.d("JapaneseWords", "Words Loaded from user with uid ${auth.currentUser?.uid}")
            }


    }



}