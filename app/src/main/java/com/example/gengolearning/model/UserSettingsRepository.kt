package com.example.gengolearning.model

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.gengolearning.words.LanguageWords
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

private const val User_Preferences_Name = "user_preferences"
private val Context.dataStore by preferencesDataStore(User_Preferences_Name)

class UserSettingsRepository @Inject constructor(
    @ApplicationContext private val  context: Context
) {

    private val LANGUAGE_KEY= stringPreferencesKey("main_language")

    val languages = Languages.languagesList



     private val _language = MutableStateFlow(languages[0].code)
    val language = _language.asStateFlow()

    //Convert the language code to language object
    val selectedLanguage: Flow<Language> = language.map{ code->
        languages.first{it.code == code}}

    val profileName = mutableStateOf<String?>(null)


   suspend fun setLanguage(selectedLanguage: String?) {
        if (selectedLanguage != null) {
            _language.value= selectedLanguage
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
    fun getMainLanguage(): Flow<String> = context.dataStore.data.map {
        it[LANGUAGE_KEY] ?: "jp"
    }
    suspend fun setMainLanguage(language: String){
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language
        }
    }

    suspend fun loadMainLanguage(){
       val savedLanguage = getMainLanguage().first()
            _language.value = savedLanguage

    }


}