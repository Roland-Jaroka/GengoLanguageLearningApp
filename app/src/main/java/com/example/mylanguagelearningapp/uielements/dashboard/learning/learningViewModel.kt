package com.example.mylanguagelearningapp.uielements.dashboard.learning

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylanguagelearningapp.grammar.ChineseGrammar
import com.example.mylanguagelearningapp.grammar.JapaneseGrammar
import com.example.mylanguagelearningapp.model.Grammar
import com.example.mylanguagelearningapp.model.UserSettingsRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LearningViewModel: ViewModel() {

    var grammars by mutableStateOf(listOf<Grammar>())
        private set
    var search by mutableStateOf("")
        private set

    fun onSearchValueChange(newValue: String) {
        search = newValue
    }

    val filteredGrammar by derivedStateOf{
        if (search.isBlank()) {
            grammars.sortedBy { it.grammar }
        } else {
            grammars.filter{ grammar ->
                listOf(grammar.grammar, grammar.explanation).any{
                    it.contains(search, ignoreCase = true)
                }
            }.sortedBy { it.grammar }
        }


    }

    init {
        snapshotFlow { UserSettingsRepository.language.value}
            .onEach { updateGrammarList(it)}
            .launchIn(viewModelScope)
    }

    fun updateGrammarList(currentLanguage: String){
        if (currentLanguage=="jp"){
            grammars= JapaneseGrammar.grammarList
        }
        else if (currentLanguage=="cn"){
            grammars= ChineseGrammar.grammarList
        }

    }

    fun loadData(currentLanguage: MutableState<String>){
        if (currentLanguage.value=="jp"){
        JapaneseGrammar.loadGrammar()}

        else if(currentLanguage.value=="cn") {
            ChineseGrammar.loadGrammar()}

    }


}