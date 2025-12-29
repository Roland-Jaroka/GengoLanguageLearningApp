package com.example.gengolearning.ui.features.autchentication.forgotpassword

sealed class ResetPasswordError {
    object InvalidUser: ResetPasswordError()
    object NoInternet: ResetPasswordError()
    object UnknownError: ResetPasswordError()
}