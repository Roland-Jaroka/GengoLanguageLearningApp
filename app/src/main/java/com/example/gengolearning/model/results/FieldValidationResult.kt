package com.example.gengolearning.model.results

sealed class FieldValidationResult {
    object Success: FieldValidationResult()
    object BlankEmail: FieldValidationResult()
    object BlankPassword: FieldValidationResult()
    object BlankConfirmPassword: FieldValidationResult()
    object BlankName: FieldValidationResult()
    object InvalidEmail: FieldValidationResult()
    object InvalidPassword: FieldValidationResult()
    object PasswordMismatch: FieldValidationResult()
    object Error: FieldValidationResult()

}