package com.example.gengolearning.ui.features.dashboard.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.data.repositories.LanguageGrammar
import com.example.gengolearning.model.AppSettingsPreferences
import com.example.gengolearning.data.repositories.UserSettingsRepository
import com.example.gengolearning.data.repositories.LanguageWords
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
    private val repository: LanguageWords,
    private val grammarRepository: LanguageGrammar
): ViewModel(){

    val currentLanguage = userSettingsRepository.selectedLanguage

    fun sendFeedback(context: Context){
        val recipient = "jaroka.roland@gmail.com"
        val subject= "Feedback"
        val body = """"
            Dear Gen-Go app team
            
            I have the following suggestion to the app/ I experienced the following bug
            
            
            
            """.trimIndent()

        val intent= Intent(Intent.ACTION_SENDTO).apply{
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        context.startActivity(Intent.createChooser(intent,"Send feedback"))

    }

    fun openAppLanguages(context: Context){
        val intent= Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent, null)
    }

    fun clearUserPreferences(context: Context){
        viewModelScope.launch {
            AppSettingsPreferences.clearAll(context)
            repository.clearWords()
            grammarRepository.clearGrammar()
        }
        }


}