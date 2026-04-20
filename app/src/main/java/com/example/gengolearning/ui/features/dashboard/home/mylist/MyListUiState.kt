package com.example.gengolearning.ui.features.dashboard.home.mylist

import com.example.gengolearning.model.appmodels.WordCategories
import com.example.gengolearning.model.appmodels.Words

data class MyListUiState(
    val words: List<Words> = emptyList(),
    val categories: List<WordCategories> = emptyList(),
    val searchInput: String = "",
    val longTappedWord: Words? = null,
    val longTappedCategory: WordCategories? = null,
    val showDeleteDialog: Boolean = false,
    val onEdit: Boolean = false,
    val selectedWords: List<String> = emptyList(),
    val selectedCategories: List<String> = emptyList(),
    val longTap: Boolean = false,
    val categoryLongTap: Boolean = false,
    val newCategoryModal: Boolean = false,
    val showCategoryBottomSheet: Boolean = false,
    val categoryListView: Boolean = false,
    val showCategoryDeleteDialog: Boolean = false,
    val categoryToDelete: WordCategories? = null,
    val quizIsEmptyModal: Boolean = false

    )


sealed class MyListUiEvents{
    object NavigateToAddWords: MyListUiEvents()
    object NavigateToNewCategory: MyListUiEvents()

    object NavigateToQuiz: MyListUiEvents()
}

sealed interface MyListActions{
    data class OnInputChanged(val newInput: String): MyListActions
    data object OnEdit: MyListActions
    data class OnToggleSelection(val id: String): MyListActions
    data class OnToggleCategorySelection(val categoryName: String, val wordId: List <Words>): MyListActions
    data object OnSelectAll: MyListActions
    data object OnRemove: MyListActions
    data class OnDeleteCategory( val category: WordCategories): MyListActions
    data object OnSendWordsToDrawingQuiz: MyListActions
    data object OnSendWordsToQuiz: MyListActions
    data object OnHomePage: MyListActions
    data object OnHomeCard: MyListActions
    data class OnLongTap(val word: Words): MyListActions
    data class OnCategoryLongTap(val category: WordCategories): MyListActions
    data class OnAddCategoryToSelectedWords(val category: String): MyListActions
    data object OnEditWord: MyListActions
    data class OnCategoryDeleteButton(val category: WordCategories): MyListActions
    data object OnDeleteCategoryButton: MyListActions
    data object OnAddCategoryButton: MyListActions
    data object OnListViewChange: MyListActions

    data object OnDismissWordLongTapModal: MyListActions

    data object OnDismissCategoryLongTapModal: MyListActions

    data object OnDismissNewCategoryModal: MyListActions

    data object OnHideDeleteWordDialog: MyListActions

    data object OnDismissCategoryDeleteDialog: MyListActions

    data object OnDismissCategoryBottomSheet: MyListActions

    data object OnEditCategory: MyListActions

    data object OnDismissQuizIsEmptyModal: MyListActions

}
