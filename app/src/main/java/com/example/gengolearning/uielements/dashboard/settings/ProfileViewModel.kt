package com.example.gengolearning.uielements.dashboard.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.grammar.LanguageGrammar
import com.example.gengolearning.model.Grammar
import com.example.gengolearning.model.UserSettingsRepository
import com.example.gengolearning.words.LanguageWords
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ProfileState{
    object Loading: ProfileState()
    object Normal: ProfileState()
}
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: LanguageWords,
    private val grammarRepository: LanguageGrammar,
    private val userSettingsRepository: UserSettingsRepository
): ViewModel() {

    val wordsList = repository.words.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    private val _grammar = MutableStateFlow<List<Grammar>>(emptyList())
    val grammar = _grammar.asStateFlow()

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState = _profileState.asStateFlow()


    val currentLanguage = userSettingsRepository.selectedLanguage
    val profileName = userSettingsRepository.profileName.value

    var wordCount by mutableStateOf(0)
        private set

    var languageCount by mutableStateOf(0)
        private set


    init {
        grammarRepository.grammar.onEach { list->
            _grammar.value = list
        }.launchIn(viewModelScope)
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            wordCount = repository.getWordCount()
            languageCount = repository.getLanguageCount()
            delay(800)
            _profileState.value = ProfileState.Normal
        }
    }
}