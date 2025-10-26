package com.example.mylanguagelearningapp.uielements.dashboard.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylanguagelearningapp.grammar.ChineseGrammar
import com.example.mylanguagelearningapp.grammar.JapaneseGrammar
import com.example.mylanguagelearningapp.model.UserSettingsRepository
import kotlinx.coroutines.launch


class LearningLanguageViewModel: ViewModel() {

    fun setLanguage(selectedLanguage: String) {
        UserSettingsRepository.setLanguage(selectedLanguage)
        if (selectedLanguage=="jp"){
            JapaneseGrammar.loadGrammar()
        }
        else if (selectedLanguage=="cn"){
            ChineseGrammar.loadGrammar()
        }
    }

    fun setMainLanguage(context: Context, selectedLanguage: String){
        viewModelScope.launch {
                UserSettingsRepository.setMainLanguage( context = context, selectedLanguage)

        }
    }


}