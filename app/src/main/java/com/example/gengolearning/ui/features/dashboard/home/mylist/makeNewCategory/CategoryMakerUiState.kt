package com.example.gengolearning.ui.features.dashboard.home.mylist.makeNewCategory

import androidx.compose.ui.graphics.Color
import com.example.gengolearning.ui.theme.White

data class CategoryMakerUiState(
    val category: String = "",
    val color: Color = White,
    val error: ErrorTypes? = null,
)

sealed class ErrorTypes {
    object isBlank : ErrorTypes()
    object categoryIsExist : ErrorTypes()
}

sealed class UiEvent {
    object CategoryCreated: UiEvent()
}
