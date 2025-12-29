package com.example.gengolearning.ui.features.autchentication.login

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

object LoginErrorMapper {

    fun map(throwable: Throwable): LoginError {
        return when (throwable) {
            is FirebaseNetworkException -> LoginError.NoInternet
            is FirebaseAuthInvalidCredentialsException -> LoginError.InvalidCredentials
            is FirebaseAuthInvalidUserException -> LoginError.InvalidUser
            else -> LoginError.UnknownError

        }


    }
}