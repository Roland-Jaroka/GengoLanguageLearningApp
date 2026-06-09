package com.example.gengolearning.model.errors

sealed interface NetworkError: Error {
    enum class GeminaiNetworkError: NetworkError{
        TOO_MANY_REQUEST,
        HEAVY_SERVERS,
        RATE_LIMIT_REACHED,
        UNKOWN_ERROR,

        NO_INTERNET
    }

    enum class BasicNetworkError: NetworkError {
        NO_INTERNET,
        SERVER_DOWN,
        RATE_LIMIT_REACHED,
        UNKOWN_ERROR
    }
}