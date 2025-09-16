package com.example.mylanguagelearningapp.model.results

sealed class AddWordResults {
    object Success: AddWordResults()
    object BlankWord: AddWordResults()
    object BlankTranslation: AddWordResults()

    object Error: AddWordResults()
}