package com.example.gengolearning.data.repositories

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.gengolearning.model.appmodels.Language
import com.example.gengolearning.model.appmodels.Languages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
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

    private val USERNAME_KEY= stringPreferencesKey("username")


    val languages = Languages.languagesList



     private val _language = MutableStateFlow(languages[0].code)
    val language = _language.asStateFlow()

    //Convert the language code to language object
    val selectedLanguage: Flow<Language> = language.map{ code->
        languages.first{it.code == code}}

    val profileName = mutableStateOf<String>("")
    val username: Flow<String> = context.dataStore.data.map {
        preferences -> preferences[USERNAME_KEY] ?: ""
    }







   suspend fun setLanguage(selectedLanguage: String?) {
        if (selectedLanguage != null) {
            _language.value= selectedLanguage
        }

        println("Language set to: ${language.value}")
    }

    suspend fun getUserData() {
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid ?: return
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

    suspend fun getUserName(){
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        if (auth.currentUser == null) return

        try {


            val userName = db.collection("users")
                .document(uid)
                .get().await()
                .getString("name")

            context.dataStore.edit { preferences ->
                preferences[USERNAME_KEY] = userName ?: ""
            }

            println("Username is fetched")
        } catch (e: FirebaseFirestoreException) {
            if (e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                return
            }
        }



    }


}