package com.example.gengolearning.uielements.dashboard.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.model.UserSettingsRepository
import kotlinx.coroutines.launch


class LearningLanguageViewModel: ViewModel() {

    fun setLanguage(selectedLanguage: String) {
        UserSettingsRepository.setLanguage(selectedLanguage)
    }

    fun setMainLanguage(context: Context, selectedLanguage: String){
        viewModelScope.launch {
                UserSettingsRepository.setMainLanguage( context = context, selectedLanguage)

        }
    }


}