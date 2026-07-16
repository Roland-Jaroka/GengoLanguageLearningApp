package com.example.gengolearning.ui.features.dashboard.settings

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.data.repositories.LanguageGrammar
import com.example.gengolearning.data.repositories.LanguageWords
import com.example.gengolearning.data.repositories.UserSettingsRepository
import com.example.gengolearning.model.AppSettingsPreferences
import com.example.gengolearning.ui.theme.AppColorTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
    private val repository: LanguageWords,
    private val grammarRepository: LanguageGrammar
): ViewModel(){

    val currentLanguage = userSettingsRepository.selectedLanguage


    val profileImage = userSettingsRepository.profileImage.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val currentTheme = userSettingsRepository.theme.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = AppColorTheme.BASIC
    )

    private val _state = MutableStateFlow(SettingUiState())
    val state = _state.asStateFlow()

    fun sendFeedback(context: Context){
        val recipient = "jaroka.roland@gmail.com"
        val subject= "Feedback"
        val body = """ Dear Gen-Go app team
            
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

    fun clearUserPreferences(context: Context){
        viewModelScope.launch {
            AppSettingsPreferences.clearAll(context)
            repository.clearWords()
            grammarRepository.clearGrammar()
            repository.clearCategories()
            if (profileImage.value != null) {
                userSettingsRepository.deleteProfilePicture(profileImage.value!!)
            }
        }
        }

    fun changeAppTheme(appTheme: AppColorTheme) {
        viewModelScope.launch {
            userSettingsRepository.saveTheme(appTheme)
        }
    }

    fun showAppThemeModal(){
        _state.update {
            it.copy(
                showAppThemeModal = true
            )
        }
    }

    fun dismissAppThemeModal() {
        _state.update {
            it.copy(
                showAppThemeModal = false
            )
        }
    }


}