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
import com.example.mylanguagelearningapp.grammar.LanguageGrammar
import com.example.mylanguagelearningapp.model.Grammar
import com.example.mylanguagelearningapp.model.UserSettingsRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

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
        if (currentLanguage =="jp"){
        JapaneseGrammar.loadGrammar()}

        else if(currentLanguage =="cn") {
            ChineseGrammar.loadGrammar()}

    }

    fun migrateGrammar(language: String){
        when (language){
            "jp"-> {
                val oldList = JapaneseGrammar.grammarList
                for (grammar in oldList){
                    LanguageGrammar.addGrammar("jp", grammar.examples?.firstOrNull() ?: "", grammar)
                }
            }
            "cn" -> {
                val oldList = ChineseGrammar.grammarList
                for (grammar in oldList) {
                    LanguageGrammar.addGrammar("cn", grammar.examples?.firstOrNull() ?: "", grammar)
                }
            }

        }
    }


}