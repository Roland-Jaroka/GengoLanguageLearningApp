package com.example.gengolearning.uielements.dashboard.learning

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.grammar.LanguageGrammar
import com.example.gengolearning.model.Grammar
import com.example.gengolearning.model.UserSettingsRepository
import com.example.gengolearning.model.results.AddGrammarResults
import com.gengolearning.app.R
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch


@HiltViewModel
class AddNewGrammarViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
    private val languageGrammar: LanguageGrammar
): ViewModel() {


    val currentLanguage = userSettingsRepository.language.value

    var grammar by mutableStateOf("")
        private set

    var explanation by mutableStateOf("")
        private set

    var grammarInputError by mutableStateOf<Int?>(null)
        private set

    var explanationInputError by mutableStateOf<Int?>(null)
        private set



    fun onGrammarInputChange(newValue: String) {

        grammar= newValue
        grammarInputError = null

    }



    fun onExplanationInputChange(newValue: String) {
        explanation= newValue
        explanationInputError = null
    }

    var example by mutableStateOf("")
        private set

    fun onFirstExampleChange(newValue: String){
        example= newValue

    }

    val examplerows = mutableStateListOf<String>()

    fun fieldValidation(grammar: String, explanation: String): AddGrammarResults {
        if (grammar.isBlank()) return AddGrammarResults.BlankGrammar
        if (explanation.isBlank()) return AddGrammarResults.BlankExplanation
        return AddGrammarResults.Success

    }

    fun addGrammarToList() {

        val result = fieldValidation(grammar, explanation)

        when(result){
            AddGrammarResults.BlankGrammar -> grammarInputError = R.string.grammar_input_error
            AddGrammarResults.BlankExplanation -> explanationInputError = R.string.explanation_input_error
            AddGrammarResults.Success ->
                viewModelScope.launch {

                    examplerows.add(example)

                    try {
                        val newGrammar = Grammar(
                            grammar,
                            explanation,
                            examplerows,
                            id = java.util.UUID.randomUUID().toString(),
                            language = currentLanguage
                        )
                        languageGrammar.addGrammar(currentLanguage, newGrammar)
                        grammar = ""
                        explanation = ""
                        example = ""
                        examplerows.clear()

                    } catch (e: Exception) {
                        //Handle error
                    }
                }
        }


    }


}