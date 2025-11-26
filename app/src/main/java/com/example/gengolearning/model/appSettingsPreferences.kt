package com.example.gengolearning.model

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val APP_SETTINGS_PREFERENCES = "AppSettingsPreferences"
private val Context.dataStore by preferencesDataStore(name = APP_SETTINGS_PREFERENCES)

object AppSettingsPreferences {

    // region Keys
    private val WELCOME_TUTORIAL = booleanPreferencesKey("welcome_tutorial")
    private val MY_LIST_TUTORIAL = booleanPreferencesKey("my_list_tutorial")
   // endregion

    // region Getters
    fun showWelcomeTutorial(context: Context): Flow<Boolean> {
      return context.dataStore.data.map { preferences ->
          preferences[WELCOME_TUTORIAL] ?: true
      }
    }

    fun showMyListTutorial(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[MY_LIST_TUTORIAL] ?: true
        }
    }

     // endregion

    // region Setters
    suspend fun setWelcomeTutorialShown (context: Context, shown: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[WELCOME_TUTORIAL] = shown

        }
    }


    suspend fun setMyListTutorialShown (context: Context, shown: Boolean){
        context.dataStore.edit { preferences ->
            preferences[MY_LIST_TUTORIAL] = shown
        }
    }

    // endregion

    suspend fun clearAll(context: Context){
        context.dataStore.edit { it.clear() }
    }
}