package com.example.mylanguagelearningapp.uielements.dashboard.learning

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mylanguagelearningapp.grammar.ChineseGrammar
import com.example.mylanguagelearningapp.grammar.JapaneseGrammar
import com.example.mylanguagelearningapp.model.UserSettingsRepository

class LearningViewModel: ViewModel() {

    val currentLanguage= UserSettingsRepository.language.value
    val grammars = if (currentLanguage=="jp") JapaneseGrammar.grammarList
    else if (currentLanguage=="cn") ChineseGrammar.grammarList
    else JapaneseGrammar.grammarList

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

    fun loadData(){
        JapaneseGrammar.loadGrammar()
        ChineseGrammar.loadGrammar()

    }


}