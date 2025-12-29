package com.example.gengolearning.ui.features.autchentication.login

sealed class LoginError {
    object NoInternet : LoginError()
    object InvalidCredentials : LoginError()
    object InvalidUser : LoginError()
    object UnknownError : LoginError()
}