package com.example.gengolearning.model.appmodels

import androidx.room.Entity
import kotlinx.serialization.Serializable
import androidx.room.PrimaryKey


@Serializable
@Entity
data class WordCategories(
    @PrimaryKey() val id: String = "",
    val categoryName: String,
    val color: Int? = null,
    val language: String= ""
)
