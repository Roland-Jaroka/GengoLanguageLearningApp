package com.example.gengolearning.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Grammar(
    val grammar: String = "",
    val explanation: String = "",
    val examples: List<String>? = emptyList(),
    @PrimaryKey
    val id: String = "",
    val title: String? = null,
    val language: String = ""
)
