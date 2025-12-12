package com.example.gengolearning.uielements.dashboard.learning

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.grammar.LanguageGrammar
import com.example.gengolearning.model.Grammar
import com.example.gengolearning.model.Language
import com.example.gengolearning.model.Languages
import com.example.gengolearning.model.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class LearningViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
    private val repository: LanguageGrammar
): ViewModel() {


    val currentLanguage = userSettingsRepository.selectedLanguage



    private val _grammar = MutableStateFlow<List<Grammar>>(emptyList())
    val grammar = _grammar.asStateFlow()
    var search by mutableStateOf("")
        private set

    fun onSearchValueChange(newValue: String) {
        search = newValue
    }

    init {
        repository.grammar.onEach { list ->
            _grammar.value = list
        }.launchIn(viewModelScope)
    }


}