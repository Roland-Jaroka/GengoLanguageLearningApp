package com.example.gengolearning.uielements.dashboard.learning

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.gengolearning.grammar.LanguageGrammar

class LearningViewModel: ViewModel() {

    val repository = LanguageGrammar

    var grammars = repository.grammar
    var search by mutableStateOf("")
        private set

    fun onSearchValueChange(newValue: String) {
        search = newValue
    }


    fun loadData(currentLanguage: String){
        LanguageGrammar.loadGrammar(currentLanguage)

    }


}