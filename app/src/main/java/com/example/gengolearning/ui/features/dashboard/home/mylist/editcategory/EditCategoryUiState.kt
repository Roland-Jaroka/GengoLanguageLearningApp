package com.example.gengolearning.ui.features.dashboard.home.mylist.editcategory


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.gengolearning.ui.features.dashboard.home.mylist.makeNewCategory.ErrorTypes

data class EditCategoryUiState(
    val category: String = "",
    val color: Int = Color.Unspecified.toArgb(),
    val error: ErrorTypes? = null,
    val showColorPicker: Boolean = false
    )


sealed class EditCategoryEvents {
    object Navigate: EditCategoryEvents()
}

sealed interface EditCategoryActions{
    data class OnNameChange(val newName: String) : EditCategoryActions
    data class OnColorChange(val newColor: Color): EditCategoryActions
    data class OnEdit(val categoryName: String): EditCategoryActions
    object ShowColorPicker: EditCategoryActions
}

