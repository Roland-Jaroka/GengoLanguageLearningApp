package com.example.gengolearning

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.grammar.LanguageGrammar
import com.example.gengolearning.model.UserSettingsRepository
import com.example.gengolearning.words.LanguageWords
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,

    private val repo: LanguageWords,
    private val grammarRepo: LanguageGrammar
): ViewModel() {


    init {
        loadMainLanguage()
    }
    private fun  loadMainLanguage(){

        viewModelScope.launch {
            userSettingsRepository.loadMainLanguage()
            repo.loadWords(userSettingsRepository.language.first())
            grammarRepo.loadGrammar(userSettingsRepository.language.first())


        }
        println("Main language loaded from Dashboard")
    }
}