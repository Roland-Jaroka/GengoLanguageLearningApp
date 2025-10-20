package com.example.mylanguagelearningapp.model

data class Words(
    val word: String="",
    val pronunciation: String="",
    val translation: String="",
    val id: String="",
    val label: String? = null,
    val isOnHomePage: Boolean?= false
)

