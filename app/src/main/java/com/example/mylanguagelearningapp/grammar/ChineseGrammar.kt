package com.example.mylanguagelearningapp.grammar

import androidx.compose.runtime.mutableStateListOf
import com.example.mylanguagelearningapp.model.Grammar

object ChineseGrammar {
    val grammarList = mutableStateListOf<Grammar>()

    fun loadGrammar(){
        //TODO load grammar from firebase
    }
}