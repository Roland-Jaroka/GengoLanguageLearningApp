package com.example.gengolearning.ui.features.dashboard.home.addwords

sealed interface AddWordsEvents {
    data object showSnackBar: AddWordsEvents
}