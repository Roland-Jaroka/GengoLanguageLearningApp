package com.example.mylanguagelearningapp.uielements.dashboard.settings

import androidx.lifecycle.ViewModel
import com.example.mylanguagelearningapp.model.UserSettingsRepository


class LearningLanguageViewModel: ViewModel() {
    val language= UserSettingsRepository.language

    fun setLanguage(selectedLanguage: String) {
        UserSettingsRepository.setLanguage(selectedLanguage)
    }


}