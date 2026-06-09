package com.example.gengolearning.ui.features.autchentication.login

sealed interface LoginActions {
    data class OnLogin(val email: String, val password: String): LoginActions
    data object OnForgotPassword: LoginActions
    data object OnSignUp: LoginActions
    data object OnLanguage: LoginActions
    data object OnGoogleSignUp: LoginActions
    data object OnRevealPassword: LoginActions
    data class OnEmailChange(val newInput: String): LoginActions
    data class OnPasswordChange (val newInput: String): LoginActions
    data object OnResetState: LoginActions
}