package com.example.gengolearning.uielements.dashboard.home.mylist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.gengolearning.model.UserSettingsRepository
import com.example.gengolearning.model.Words
import com.example.gengolearning.words.LanguageWords
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class EditWordViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: LanguageWords,
    userSettingsRepository: UserSettingsRepository
): ViewModel() {

   private val wordId: String = savedStateHandle["wordId"]!!

    val currentLanguage = userSettingsRepository.selectedLanguage

    val word: Words? = repository.words.value.find {
        it.id == wordId
    }

}