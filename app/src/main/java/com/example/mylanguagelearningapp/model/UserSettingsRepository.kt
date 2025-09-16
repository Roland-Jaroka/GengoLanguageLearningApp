package com.example.mylanguagelearningapp.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.mylanguagelearningapp.grammar.JapaneseGrammar
import com.example.mylanguagelearningapp.words.ChineseWords
import com.example.mylanguagelearningapp.words.JapaneseWords
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object UserSettingsRepository {


     val language = mutableStateOf("jp")
     val wordsList = mutableStateOf(JapaneseWords.wordList)
    //TODO Initiate a global List of Words based on language
    fun setLanguage(selectedLanguage: String) {
        language.value= selectedLanguage

        println("Language set to: ${language.value}")
    }
    fun getMainLanguage(){
        val auth= FirebaseAuth.getInstance()
        val uid= auth.currentUser?.uid.toString()
        val db= FirebaseFirestore.getInstance()

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { document->
                val language= document.getString("language")?: "jp"
                language.let{
                    setLanguage(it)
                }
            }
    }
}