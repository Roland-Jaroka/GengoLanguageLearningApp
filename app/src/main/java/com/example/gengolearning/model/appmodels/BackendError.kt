package com.example.gengolearning.model.appmodels

import kotlinx.serialization.Serializable

@Serializable
data class BackendError(
    val error: String,
    val status: Int
)
