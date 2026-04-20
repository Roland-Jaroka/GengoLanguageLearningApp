package com.example.gengolearning.model.appmodels

import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val imageUrl: String,
    val newsEn: String = "",
    val newsJp: String = "",
    val newsHu: String = "",
    val messageEn: String = "",
    val messageJp: String = "",
    val messageHu: String = "",
    val clickable: Boolean = false
)


