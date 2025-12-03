package com.example.gengolearning.uielements.dashboard.home

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.model.AppSettingsPreferences
import com.example.gengolearning.model.Language
import com.example.gengolearning.model.Languages
import com.example.gengolearning.model.UserSettingsRepository
import com.example.gengolearning.model.Words
import com.example.gengolearning.words.LanguageWords
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: LanguageWords,
   val userSettingsRepository: UserSettingsRepository
): ViewModel() {

    var wordsList = repository.words.map{ list->
        val homeWords = list.filter { it.isOnHomePage==true }
        if (homeWords.isEmpty()) list else homeWords
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    var currentIndex = mutableStateOf(0)


    var currentWord = mutableStateOf<Words?>(null)

    val currentLanguage= userSettingsRepository.selectedLanguage

    val selectedLanguage: StateFlow<String> = userSettingsRepository.language
    fun showTutorial(context: Context) = AppSettingsPreferences.showWelcomeTutorial(context)


    init {
        //raw repository check
        repository.words.onEach{ list ->
            currentIndex.value = 0
            currentWord.value = list.firstOrNull()

        }.launchIn(viewModelScope)

        //after updating with HomePage
        viewModelScope.launch {
            wordsList.collect{ list->
                if (currentIndex.value >= list.size) {
                    currentIndex.value = 0
                }
                currentWord.value = list.getOrNull(currentIndex.value)
            }
        }
    }


    fun onNextClick() {
        val list = wordsList.value

        if (list.isEmpty()) return

        currentIndex.value = (currentIndex.value + 1) % list.size

        currentWord.value = list[currentIndex.value]
    }

    fun onPreviousClick() {

        val list = wordsList.value

        if (list.isEmpty()) return
        currentIndex.value = (currentIndex.value - 1 + list.size) % list.size
        currentWord.value = list[currentIndex.value]
    }

    var isWordVisible by mutableStateOf(true)
    var isPronunciationVisible by mutableStateOf(true)
    var isTranslationVisible by mutableStateOf(true)

    fun onWordClick() {
        isWordVisible = !isWordVisible
    }

    fun onPronunciationClick() {
        isPronunciationVisible = !isPronunciationVisible
    }

    fun onTranslationClick() {

        isTranslationVisible = !isTranslationVisible
    }

    fun setWelcomeTutorial(context: Context){
        viewModelScope.launch {
            AppSettingsPreferences.setWelcomeTutorialShown(context, false)
        }
    }
}

