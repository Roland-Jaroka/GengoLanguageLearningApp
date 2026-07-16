package com.example.gengolearning.ui.features.dashboard.home.mylist.editword

import com.example.gengolearning.model.appmodels.WordCategories

sealed interface EditWordActions {
    data class OnCategoryClick(val categories: WordCategories): EditWordActions
    data class OnDeletableCategoryClick(val categories: WordCategories): EditWordActions
    data class OnWordInputChange(val newInput: String): EditWordActions
    data class OnPronunciationInputChange(val newInput: String): EditWordActions
    data class OnTranslationInputChange(val newInput: String): EditWordActions
    data class OnUpdate(val currentLanguage: String): EditWordActions



}