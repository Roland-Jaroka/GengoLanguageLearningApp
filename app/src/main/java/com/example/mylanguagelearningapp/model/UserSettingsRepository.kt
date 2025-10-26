package com.example.mylanguagelearningapp.model

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mylanguagelearningapp.words.JapaneseWords
import com.example.mylanguagelearningapp.words.LanguageWords
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import com.example.mylanguagelearningapp.model.Language
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn


object UserSettingsRepository {

    private const val User_Preferences_Name = "user_preferences"
    private val Context.dataStore by preferencesDataStore(User_Preferences_Name)

    private val LANGUAGE_KEY= stringPreferencesKey("main_language")

    val languages = Languages.languagesList



     private val _language = MutableStateFlow(languages[0].code)
    val language: StateFlow<String> = _language

    val selectedLanguage: StateFlow<Language> = language.map{code->
        languages.first{it.code == code}}.stateIn(
            scope = CoroutineScope(Dispatchers.Main),
            started = SharingStarted.Eagerly,
            initialValue = languages[0]
        )

   private val _mainLanguage = MutableStateFlow("jp")
    val mainLanguage = _mainLanguage.asStateFlow()

    val profileName = mutableStateOf<String?>(null)


    fun setLanguage(selectedLanguage: String?) {
        if (selectedLanguage != null) {
            _language.value= selectedLanguage
            LanguageWords.loadWords(selectedLanguage)
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

            val name= document.getString("name")?: ""

               profileName.value = name


        } catch (e:Exception){
            e.printStackTrace()
        }

    }
    fun getMainLanguage(context: Context): Flow<String> = context.dataStore.data.map {
        it[LANGUAGE_KEY] ?: "jp"
    }
    suspend fun setMainLanguage(context: Context, language: String){
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language
        }
    }

    suspend fun loadMainLanguage(context: Context){
        getMainLanguage(context).collect { savedLanguage ->
            _language.value = savedLanguage
            LanguageWords.loadWords(savedLanguage)
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

        }catch (e: Exception){
            e.printStackTrace()
        }

    }


}