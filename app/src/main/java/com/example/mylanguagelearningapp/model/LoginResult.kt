package com.example.mylanguagelearningapp.model

sealed class LoginResult {
    object Success: LoginResult()
    object BlankEmail: LoginResult()
    object BlankPassword: LoginResult()
    object InvalidEmail: LoginResult()
    object Error: LoginResult()
    data class UnknownError(val message: String): LoginResult()

}