package com.example.gengolearning.ui.features.autchentication.signup

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

object SignUpErrorMapper {

    fun map(throwable: Throwable): SignUpErrors {
        return when(throwable) {
            is FirebaseAuthWeakPasswordException -> SignUpErrors.WeakPassword
            is FirebaseAuthInvalidCredentialsException -> SignUpErrors.MalformedEmail
            is FirebaseAuthUserCollisionException -> SignUpErrors.EmailAlreadyInUse
            is FirebaseNetworkException -> SignUpErrors.NoInternet
            else -> SignUpErrors.UnknownError

        }
    }
}