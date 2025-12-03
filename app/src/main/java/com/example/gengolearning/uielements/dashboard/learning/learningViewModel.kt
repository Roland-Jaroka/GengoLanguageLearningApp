package com.example.gengolearning.uielements.dashboard.learning

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.grammar.LanguageGrammar
import com.example.gengolearning.model.Language
import com.example.gengolearning.model.Languages
import com.example.gengolearning.model.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class LearningViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
): ViewModel() {

    val repository = LanguageGrammar

    val currentLanguage = userSettingsRepository.selectedLanguage

    var grammars = repository.grammar
    var search by mutableStateOf("")
        private set

    fun onSearchValueChange(newValue: String) {
        search = newValue
    }


    fun loadData(currentLanguage: String){
        LanguageGrammar.loadGrammar(currentLanguage)

    }


}