package com.example.gengolearning.uielements.dashboard.learning

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.grammar.LanguageGrammar
import com.example.gengolearning.model.UserSettingsRepository
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

sealed class ChatGPTState{
    object Loading: ChatGPTState()
    class Success(val response: String): ChatGPTState()
    class Error(val message: String): ChatGPTState()
}
@HiltViewModel
class GrammarDetailsViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
): ViewModel() {



    val currentLanguage = userSettingsRepository.selectedLanguage.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        userSettingsRepository.languages[0]
    )

    fun addNewExample(language: String, grammarid: String?, exampleText: String) {

        LanguageGrammar.addNewExample(language, grammarid, exampleText)



    }

    fun onSave(grammarid: String?, language: String, grammar: String, explanation: String){

        LanguageGrammar.onSave(language, grammarid, grammar, explanation)


    }

    fun onExampleDelete(language: String, grammarId: String?, exampleRows: List<String>, index: Int) {

        LanguageGrammar.onExampleRemove(language, grammarId, exampleRows, index)

    }

    fun onRemove(language: String, grammarid: String?){

        LanguageGrammar.onRemove(language, grammarid)

    }

    private val _chatGPTState = MutableStateFlow<ChatGPTState>(ChatGPTState.Loading)
    val chatGPTState = _chatGPTState.asStateFlow()

    suspend fun geminAiGrammar(grammar: String) {

        _chatGPTState.value = ChatGPTState.Loading

        try {
            val model = Firebase.ai(backend = GenerativeBackend.googleAI())
                .generativeModel("gemini-2.5-flash")
            val prompt =
                "Give me an example sentence from ${currentLanguage.value.name} language using the following grammar: $grammar and use English for explanation"
            val response = model.generateContent(prompt)
             _chatGPTState.value = ChatGPTState.Success(response.text ?: "")
        } catch (e: Exception) {
            _chatGPTState.value = ChatGPTState.Error(e.message ?: "Unknown error")


        }
    }
}