package com.example.mylanguagelearningapp.model.results

sealed class SignupResult {

    object Success: SignupResult()

    //Blank:
    object BlankName: SignupResult()
    object BlankEmail: SignupResult()
    object BlankPassword: SignupResult()
    object BlankConfirmPassword: SignupResult()

    //Invalid:
    object InvalidEmail: SignupResult()
    object InvalidPassword: SignupResult()
    object PasswordMismatch: SignupResult()

    //Error:
    object Error: SignupResult()
    data class UnknownError(val message: String): SignupResult()

}