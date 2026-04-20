package com.example.gengolearning.ui.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.data.repositories.LanguageGrammar
import com.example.gengolearning.data.repositories.UserSettingsRepository
import com.example.gengolearning.data.repositories.LanguageWords
import com.google.firebase.auth.FirebaseAuth
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

        if (FirebaseAuth.getInstance().currentUser == null) return

        viewModelScope.launch {

            userSettingsRepository.loadMainLanguage()

            launch {
                repo.loadWords(userSettingsRepository.language.first())
                repo.loadCategories(userSettingsRepository.language.first())
            }

            launch {  grammarRepo.loadGrammar(userSettingsRepository.language.first()) }



        }

    }
}