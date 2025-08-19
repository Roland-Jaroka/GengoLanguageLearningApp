package com.example.mylanguagelearningapp.model

sealed class ResetPasswordResult {
    object Success: ResetPasswordResult()
    object BlankEmail: ResetPasswordResult()
    object InvalidEmail: ResetPasswordResult()
    data class Error (val message: String): ResetPasswordResult()
}