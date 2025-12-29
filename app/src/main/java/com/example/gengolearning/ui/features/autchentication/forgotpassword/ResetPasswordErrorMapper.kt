package com.example.gengolearning.ui.features.autchentication.forgotpassword

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

object ResetPasswordErrorMapper {

    fun map(throwable: Throwable): ResetPasswordError {
        return when(throwable){
            is FirebaseAuthInvalidUserException -> ResetPasswordError.InvalidUser
            is FirebaseNetworkException -> ResetPasswordError.NoInternet
            else -> ResetPasswordError.UnknownError

        }
    }
}