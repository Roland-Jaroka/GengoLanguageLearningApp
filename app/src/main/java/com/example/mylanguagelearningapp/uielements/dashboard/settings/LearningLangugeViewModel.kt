package com.example.mylanguagelearningapp.uielements.dashboard.settings

import androidx.lifecycle.ViewModel
import com.example.mylanguagelearningapp.grammar.ChineseGrammar
import com.example.mylanguagelearningapp.grammar.JapaneseGrammar
import com.example.mylanguagelearningapp.model.UserSettingsRepository


class LearningLanguageViewModel: ViewModel() {
    val language= UserSettingsRepository.language

    fun setLanguage(selectedLanguage: String) {
        UserSettingsRepository.setLanguage(selectedLanguage)

        if (selectedLanguage=="jp"){
            JapaneseGrammar.loadGrammar()
        }
        else if (selectedLanguage=="cn"){
            ChineseGrammar.loadGrammar()
        }
    }


}