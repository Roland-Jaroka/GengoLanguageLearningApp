package com.example.gengolearning.uielements.dashboard.settings

import androidx.lifecycle.ViewModel
import com.example.gengolearning.model.UserSettingsRepository
import com.example.gengolearning.words.LanguageWords
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: LanguageWords,
    private val userSettingsRepository: UserSettingsRepository
): ViewModel() {

    val wordsList = repository.words
    val currentLanguage = userSettingsRepository.selectedLanguage
    val profileName = userSettingsRepository.profileName.value


}