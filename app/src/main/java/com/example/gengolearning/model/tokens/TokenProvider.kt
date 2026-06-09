package com.example.gengolearning.model.tokens

interface TokenProvider {
    suspend fun getToken(): String?
}