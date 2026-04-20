package com.example.gengolearning.ui.features.dashboard.home.mylist.editcategory


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.gengolearning.ui.features.dashboard.home.mylist.makeNewCategory.ErrorTypes

data class EditCategoryUiState(
    val category: String = "",
    val color: Int = Color.Unspecified.toArgb(),
    val error: ErrorTypes? = null
    )


sealed class EditCategoryEvents {
    object Navigate: EditCategoryEvents()
}

