package com.example.gengolearning.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Words(
    val word: String="",
    val pronunciation: String="",
    val translation: String="",
    @PrimaryKey
    val id: String="",
    val label: String? = null,
    val isOnHomePage: Boolean?= false
)

