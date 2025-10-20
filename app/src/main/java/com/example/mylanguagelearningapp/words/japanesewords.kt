package com.example.mylanguagelearningapp.words

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.mylanguagelearningapp.model.Words
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore


object JapaneseWords {
    val wordList = mutableStateListOf<Words>()

    var loaded = mutableStateOf(false)



    val auth= FirebaseAuth.getInstance()

    var error = ""

  fun loadWords() {
        val uid= auth.currentUser?.uid.toString()
        if (uid.isEmpty()) return

        Firebase.firestore
            .collection("users")
            .document(uid)
            .collection("words")
            .get().addOnSuccessListener {
                wordList.clear()
                loaded.value = true

                for (document in it) {
                    val word = document.getString("word") ?: ""
                    val pronunciation = document.getString("pronunciation") ?: ""
                    val translation = document.getString("translation") ?: ""
                    val label = document.getString("label")
                    val isOnHomePage = document.getBoolean("isOnHomePage") ?: false
                    val id = document.id
                    wordList.add(Words(word, pronunciation, translation, id, label, isOnHomePage))
                }

                Log.d("JapaneseWords", "Loaded ${wordList.size} words")
                Log.d("JapaneseWords", "Words Loaded from user with uid ${auth.currentUser?.uid}")
            }


    }



}