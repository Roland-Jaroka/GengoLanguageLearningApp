package com.example.gengolearning.ui.features.dashboard.learning.grammarDetails

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.data.repositories.LanguageGrammar
import com.example.gengolearning.data.repositories.UserSettingsRepository
import com.example.gengolearning.model.appmodels.Grammar
import com.example.gengolearning.model.results.Response
import com.gengolearning.app.R
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class ChatGPTState{
    object Loading: ChatGPTState()
    class Success(val response: String): ChatGPTState()
    class Error(val message: String): ChatGPTState()
}
@HiltViewModel
class GrammarDetailsViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
    private val languageGrammar: LanguageGrammar,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {


        private val grammarId: String = savedStateHandle["grammarId"]!!

    val currentLanguage = userSettingsRepository.selectedLanguage.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        userSettingsRepository.languages[0]
    )

    val grammar: StateFlow <List<Grammar>> = languageGrammar.grammar.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    val appLanguage = AppCompatDelegate.getApplicationLocales()[0]?.toLanguageTag() ?: "en"

    private val _editGrammarState = MutableStateFlow(EditGrammarState())
    val editGrammarState = _editGrammarState.asStateFlow()


   fun addNewExample(language: String, grammarid: String?, exampleText: String) {
       viewModelScope.launch {
           val currentGrammar = grammar.value.find { it.id == grammarId }

           languageGrammar.addNewExample(language, grammarid, exampleText, currentGrammar)
       }

    }


    fun onExampleDelete(language: String, grammarId: String?, exampleRows: List<String>, index: Int) {

        viewModelScope.launch {
            val currentGrammar = grammar.value.find { it.id == grammarId }
            languageGrammar.onExampleRemove(language, grammarId, exampleRows, index, currentGrammar)
        }

    }

    fun onRemove(language: String, grammarid: String){
         viewModelScope.launch {
             languageGrammar.onRemove(language, grammarid)
         }
    }

    private val _chatGPTState = MutableStateFlow<ChatGPTState>(ChatGPTState.Loading)
    val chatGPTState = _chatGPTState.asStateFlow()

    suspend fun geminAiGrammar(grammar: String, currentLanguage: String) {

        _chatGPTState.value = ChatGPTState.Loading

//        try {
//            val model = Firebase.ai(backend = GenerativeBackend.googleAI())
//                .generativeModel("gemini-2.5-flash")
//            val prompt =
//                "Give me an example sentence from $currentLanguage language using the following grammar: $grammar and use $appLanguage for explanation"
//            val response = model.generateContent(prompt)
//             _chatGPTState.value = ChatGPTState.Success(response.text ?: "")
//        } catch (e: Exception) {
//            _chatGPTState.value = ChatGPTState.Error(e.message ?: "Unknown error")
//
//            println("The error is: ${e.message}")
//
//
//
//        }

        val response = languageGrammar.getGeminiaiGrammar(
            language = currentLanguage,
            grammarTopic = grammar
        )

        when(response) {
            is Response.Success -> {
                _chatGPTState.value = ChatGPTState.Success(response.data.explanation)
            }
            is Response.Error -> {
                _chatGPTState.value = ChatGPTState.Error(response.error.toString())
            }
        }

    }

    //Edit grammar screen logic
    fun onEditGrammarDialog(){
        _editGrammarState.update {
            it.copy(
                showDialog = true
            )
        }

        resetGrammarEditState()
    }

    fun dismissEditGrammarDialog(){
        _editGrammarState.update {
            it.copy(
                showDialog = false
            )
        }
    }

    fun onEditGrammarTitleChange(input: String) {
        _editGrammarState.update {
            it.copy(
                title = input,
                titleFieldValidation = false,
                titleFieldValidationMessage = null
            )
        }
    }

    fun onEditGrammarSummaryChange(input: String) {
        _editGrammarState.update {
            it.copy(
                summary = input,
                summaryFieldValidation = false,
                summaryFieldValidationMessage = null
            )
        }
    }

  private  fun fieldValidation(title: String, summary: String): Boolean {
        return when {
            title.isEmpty() -> {
                _editGrammarState.update {
                    it.copy(
                        titleFieldValidation = true,
                        titleFieldValidationMessage = R.string.grammar_input_error
                    )
                }
                false
            }

            summary.isEmpty() -> {
                _editGrammarState.update {
                    it.copy(
                        summaryFieldValidation = true,
                        summaryFieldValidationMessage = R.string.explanation_input_error
                    )
                }
                false
            }
            else -> true
        }
    }

    fun onSave(grammarid: String?, language: String, grammarTitle: String, explanation: String){

        if (!fieldValidation(grammarTitle, explanation)) return

       viewModelScope.launch {
            val currentGrammar = grammar.value.find { it.id == grammarid }
            languageGrammar.onSave(language, grammarid, grammarTitle, explanation, currentGrammar)

           dismissEditGrammarDialog()

        }






    }

    fun onStart() {
        val currentGrammar = grammar.value.find { it.id == grammarId }
        _editGrammarState.update {
            it.copy(
                title = currentGrammar?.grammar ?: "",
                summary = currentGrammar?.explanation ?: ""

            )
        }
    }

    fun resetGrammarEditState() {
        _editGrammarState.update {
            it.copy(
                title = "",
                summary = "",
                titleFieldValidation = false,
                summaryFieldValidation = false,
                titleFieldValidationMessage = null,
                summaryFieldValidationMessage = null,
            )
        }
    }

}