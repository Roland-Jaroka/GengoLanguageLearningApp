package com.example.gengolearning.model.results

import com.example.gengolearning.model.errors.Error

typealias RootError = Error
sealed interface Response<out D, out E: RootError> {
    data class Success<out D>(val data: D): Response<D, Nothing>
    data class Error<out E: RootError>(val error: E): Response<Nothing,E>
}