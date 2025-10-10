package com.example.mylanguagelearningapp.model

import androidx.compose.runtime.mutableStateOf
import com.example.mylanguagelearningapp.words.JapaneseWords
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

object UserSettingsRepository {


     val language = mutableStateOf("jp")
    val mainLanguage = mutableStateOf<String?>(null)

    val profileName = mutableStateOf<String?>(null)

    private val _dataLoaded = MutableStateFlow(false)
    val dataLoaded: StateFlow<Boolean> = _dataLoaded.asStateFlow()

    val firstLogin = mutableStateOf<Boolean?>(null)
     val wordsList = mutableStateOf(JapaneseWords.wordList)
    //TODO Initiate a global List of Words based on language
    fun setLanguage(selectedLanguage: String?) {
        if (selectedLanguage != null) {
            language.value= selectedLanguage
        }

        println("Language set to: ${language.value}")
    }

    suspend fun getUserData() {
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid.toString()
        val db = FirebaseFirestore.getInstance()

        try {
            val document = db.collection("users")
                .document(uid)
                .get().await()

            val language = document.getString("language") ?: "jp"
            val name= document.getString("name")?: ""


                setLanguage(language)
                mainLanguage.value = language

            profileName.value = name




            _dataLoaded.value = true
        } catch (e:Exception){
            e.printStackTrace()
        }

    }

   suspend fun setMainLanguage(selectedLanguage: String?){
        val auth= FirebaseAuth.getInstance()
        val uid= auth.currentUser?.uid.toString()
        val db= FirebaseFirestore.getInstance()
       try {
           db.collection("users")
               .document(uid)
               .update("language", selectedLanguage)
               .await()
           setLanguage(selectedLanguage)
       }catch (e: Exception){
           e.printStackTrace()
       }

    }

    suspend fun setFirstLogin(){
        val auth= FirebaseAuth.getInstance()
        val uid= auth.currentUser?.uid.toString()
        val db= FirebaseFirestore.getInstance()

        try {
            db.collection("users")
                .document(uid)
                .update("firstLogin", false)
                .await()
            this.firstLogin.value = false
        }catch (e: Exception){
            e.printStackTrace()
        }

    }


}