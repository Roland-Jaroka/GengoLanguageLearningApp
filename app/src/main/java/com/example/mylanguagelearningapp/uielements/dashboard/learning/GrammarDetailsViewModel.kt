package com.example.mylanguagelearningapp.uielements.dashboard.learning

import androidx.lifecycle.ViewModel
import com.example.mylanguagelearningapp.grammar.ChineseGrammar
import com.example.mylanguagelearningapp.grammar.JapaneseGrammar
import com.example.mylanguagelearningapp.grammar.LanguageGrammar
import com.example.mylanguagelearningapp.model.UserSettingsRepository
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed class ChatGPTState{
    object Loading: ChatGPTState()
    class Success(val response: String): ChatGPTState()
    class Error(val message: String): ChatGPTState()
}
class GrammarDetailsViewModel: ViewModel() {



    val currentLanguage = UserSettingsRepository.language.value
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
    val chatGPTState: StateFlow<ChatGPTState> = _chatGPTState

    suspend fun geminAiGrammar(grammar: String) {

        _chatGPTState.value = ChatGPTState.Loading

        try {
            val model = Firebase.ai(backend = GenerativeBackend.googleAI())
                .generativeModel("gemini-2.5-flash")
            val prompt =
                "Give me an example sentence in Japanese using the following grammar: $grammar"
            val response = model.generateContent(prompt)
             _chatGPTState.value = ChatGPTState.Success(response.text ?: "")
        } catch (e: Exception) {
            _chatGPTState.value = ChatGPTState.Error(e.message ?: "Unknown error")


        }
    }
}