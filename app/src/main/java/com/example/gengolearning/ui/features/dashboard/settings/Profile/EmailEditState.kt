package com.example.gengolearning.ui.features.dashboard.settings.Profile

data class ProfileNameEditState(
    val name: String = "",
    val isLoading: Boolean = false,
    val success: EditState = EditState.Idle,
    val error: String? = null,
    val fieldError: Boolean = false,
    val fieldValidationMessage: Int? = null
)

sealed class EditState {
    object Idle: EditState()
    object Success: EditState()
    object Failure: EditState()
}
