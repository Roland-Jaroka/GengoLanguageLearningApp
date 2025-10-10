package com.example.mylanguagelearningapp.uielements.dashboard.learning

import androidx.lifecycle.ViewModel
import com.example.mylanguagelearningapp.grammar.ChineseGrammar
import com.example.mylanguagelearningapp.grammar.JapaneseGrammar
import com.example.mylanguagelearningapp.model.UserSettingsRepository
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend

class GrammarDetailsViewModel: ViewModel() {



    val currentLanguage = UserSettingsRepository.language.value
    fun addNewExample(grammarid: String?, exampleText: String) {

        if (currentLanguage=="jp") {
            JapaneseGrammar.addNewExample(grammarid, exampleText)
        }
        else if (currentLanguage=="cn"){

            ChineseGrammar.addNewExample(grammarid, exampleText)

        }



    }

    fun onSave(grammarid: String?, grammar: String, explanation: String){

        if (currentLanguage=="jp") {
            JapaneseGrammar.onSave(grammarid, grammar, explanation)
        }
        else if (currentLanguage=="cn"){

            ChineseGrammar.onSave(grammarid, grammar, explanation)

        }

    }

    fun onExampleDelete(grammarId: String?, exampleRows: List<String>, index: Int) {
        if (currentLanguage=="jp") {
            JapaneseGrammar.onExampleDelete(grammarId, exampleRows, index)
        }
        else if (currentLanguage=="cn"){

            ChineseGrammar.onExampleDelete(grammarId, exampleRows, index)

        }

        println("on example delete was executed ${grammarId} + ${exampleRows} + ${index}")

    }

    fun onRemove(grammarid: String?){
        if (currentLanguage=="jp") {
            JapaneseGrammar.onRemove(grammarid)
        }
        else if (currentLanguage=="cn"){
            ChineseGrammar.onRemove(grammarid)
        }
    }

    suspend fun geminAiGrammar(grammar: String): String {


        val model = Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel("gemini-2.5-flash")
        val prompt= "Give me an example sentence in Japanese using the following grammar: $grammar"
        val response = model.generateContent(prompt)
        return response.text?:""
    }
}