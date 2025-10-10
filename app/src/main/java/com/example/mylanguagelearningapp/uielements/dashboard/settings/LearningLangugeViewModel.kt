package com.example.mylanguagelearningapp.uielements.dashboard.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylanguagelearningapp.grammar.ChineseGrammar
import com.example.mylanguagelearningapp.grammar.JapaneseGrammar
import com.example.mylanguagelearningapp.model.UserSettingsRepository
import kotlinx.coroutines.launch


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

    fun setMainLanguage(selectedLanguage: String?){
        viewModelScope.launch {
            UserSettingsRepository.setMainLanguage(selectedLanguage)
        }
    }


}