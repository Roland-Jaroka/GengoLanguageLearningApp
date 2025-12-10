package com.example.gengolearning.uielements.dashboard.home

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.model.AppSettingsPreferences
import com.example.gengolearning.model.UserSettingsRepository
import com.example.gengolearning.model.Words
import com.example.gengolearning.words.LanguageWords
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: LanguageWords,
   val userSettingsRepository: UserSettingsRepository
): ViewModel() {

    val _wordsList = MutableStateFlow<List<Words>>(emptyList())
    val wordsList = _wordsList.asStateFlow()



    var currentIndex = mutableStateOf(0)


    var currentWord = mutableStateOf<Words?>(null)

    val currentLanguage= userSettingsRepository.selectedLanguage

    val selectedLanguage: StateFlow<String> = userSettingsRepository.language
    fun showTutorial(context: Context) = AppSettingsPreferences.showWelcomeTutorial(context)


    init {
        repository.words.onEach { list->

            //if word is not empty filter words by isHomePage
            if (list.isNotEmpty()){
                val filteredList = list.filter { it.isOnHomePage == true }
                if (filteredList.isNotEmpty()) {
                    currentIndex.value = 0
                    _wordsList.value = filteredList
                    currentWord.value = wordsList.value[0]
                }

                //if filtered words is empty which is equals to there are no isOnHomePage== true
                else {
                    currentIndex.value = 0
                    _wordsList.value = list
                    currentWord.value = wordsList.value[0]
                }


            }
            //if the word list is empty
            else {
                currentIndex.value = 0
                _wordsList.value = emptyList()
                currentWord.value = null
            }

        }.launchIn(viewModelScope)

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

