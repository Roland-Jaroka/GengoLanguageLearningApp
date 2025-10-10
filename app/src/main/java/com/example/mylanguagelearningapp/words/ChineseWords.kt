package com.example.mylanguagelearningapp.words

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.mylanguagelearningapp.model.Words
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

object ChineseWords {

    val chinseWordsList = mutableStateListOf<Words>()
    val auth= FirebaseAuth.getInstance()
    val loaded = mutableStateOf(false)


    var error = ""

  fun loadWords(){
        val uid= auth.currentUser?.uid.toString()
        if (uid.isEmpty()) return

        Firebase.firestore
            .collection("users")
            .document(uid)
            .collection("chineseCollection")
            .document("chinese")
            .collection("chineseWords")
            .get().addOnSuccessListener{
                chinseWordsList.clear()
                for (document in it) {
                    val word = document.getString("word") ?: ""
                    val pronunciation = document.getString("pronunciation") ?: ""
                    val translation = document.getString("translation") ?: ""
                    val id = document.id
                    chinseWordsList.add(Words(word, pronunciation, translation, id))
                }
                loaded.value = true
            }
    }

    fun addWord(word: Words){
        val uid= auth.currentUser?.uid.toString()
        if (uid.isEmpty()) return
        Firebase.firestore
            .collection("users")
            .document(uid)
            .collection("chineseCollection")
            .document("chinese")
            .collection("chineseWords")
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
}