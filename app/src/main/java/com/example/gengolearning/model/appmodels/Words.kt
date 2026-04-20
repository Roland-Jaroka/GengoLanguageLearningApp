package com.example.gengolearning.model.appmodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Words(
    val word: String="",
    @SerialName("reading")
    val pronunciation: String="",
    val translation: String="",
    @PrimaryKey
    val id: String="",
    val isOnHomePage: Boolean?= false,
    val language: String="",
    val category: List<String> = emptyList()
)

