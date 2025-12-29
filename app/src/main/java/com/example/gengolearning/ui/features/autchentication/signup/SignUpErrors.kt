package com.example.gengolearning.ui.features.autchentication.signup

sealed class SignUpErrors {
    object WeakPassword: SignUpErrors()
    object MalformedEmail: SignUpErrors()
    object EmailAlreadyInUse: SignUpErrors()

    object NoInternet: SignUpErrors()
    object UnknownError: SignUpErrors()
}




