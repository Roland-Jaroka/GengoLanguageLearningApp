package com.example.mylanguagelearningapp.uielements.dashboard.learning

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mylanguagelearningapp.grammar.ChineseGrammar
import com.example.mylanguagelearningapp.grammar.JapaneseGrammar
import com.example.mylanguagelearningapp.grammar.LanguageGrammar
import com.example.mylanguagelearningapp.model.Grammar
import com.example.mylanguagelearningapp.model.UserSettingsRepository
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend

class AddNewGrammarViewModel: ViewModel() {


    val currentLanguage = UserSettingsRepository.language.value

    var grammar by mutableStateOf("")
        private set

    fun onGrammarInputChange(newValue: String) {

        grammar= newValue

    }

    var explanation by mutableStateOf("")
        private set

    fun onExplanationInputChange(newValue: String) {
        explanation= newValue
    }

    var example by mutableStateOf("")
        private set

    fun onFirstExampleChange(newValue: String){
        example= newValue

    }

    val examplerows = mutableStateListOf<String>()

    fun addGrammarToList(language: String) {

        LanguageGrammar.addGrammar(language, example, Grammar(grammar, explanation, examplerows, id = ""))
                grammar = ""
                explanation = ""
                example = ""
                examplerows.clear()

    }


}