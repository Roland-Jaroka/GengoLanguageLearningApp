package com.example.gengolearning.ui.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.data.repositories.LanguageGrammar
import com.example.gengolearning.data.repositories.UserSettingsRepository
import com.example.gengolearning.data.repositories.LanguageWords
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
sealed class MainApp{

    object Loading : MainApp()
    object Success: MainApp()
}
@HiltViewModel
class MainViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,

    private val repo: LanguageWords,
    private val grammarRepo: LanguageGrammar
): ViewModel() {

    private  val _state = MutableStateFlow<MainApp>(MainApp.Loading)
      val state = _state.asStateFlow()

    init {
        loadMainLanguage()
    }


    private fun  loadMainLanguage(){

        if (FirebaseAuth.getInstance().currentUser == null) return

        viewModelScope.launch {
            _state.value = MainApp.Loading

            try {


            userSettingsRepository.loadMainLanguage()

            launch {
                repo.loadWords(userSettingsRepository.language.first())
                repo.loadCategories(userSettingsRepository.language.first())
            }

            launch {  grammarRepo.loadGrammar(userSettingsRepository.language.first()) }

              _state.value = MainApp.Success } catch (e: Exception) {
                  _state.value = MainApp.Success
              }

        }

    }
}