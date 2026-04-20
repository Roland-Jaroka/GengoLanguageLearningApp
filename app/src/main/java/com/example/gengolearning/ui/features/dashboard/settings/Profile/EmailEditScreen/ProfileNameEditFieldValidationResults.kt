package com.example.gengolearning.ui.features.dashboard.settings.Profile.EmailEditScreen

sealed class ProfileNameEditFieldValidationResults {
    object Success: ProfileNameEditFieldValidationResults()
    object Empty: ProfileNameEditFieldValidationResults()
    object TooLong: ProfileNameEditFieldValidationResults()
    object SameAsBefore: ProfileNameEditFieldValidationResults()

}