package com.example.gengolearning.ui.features.dashboard.home.mylist.editcategory


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.data.repositories.LanguageWords
import com.example.gengolearning.model.appmodels.WordCategories
import com.example.gengolearning.ui.features.dashboard.home.mylist.makeNewCategory.ErrorTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EditCategoryViewModel @Inject constructor(
    saveStateHandle: SavedStateHandle,
    private val repository: LanguageWords
): ViewModel() {

    private val categoryId: String = saveStateHandle["categoryId"]!!

    val _category = MutableStateFlow<WordCategories?>(null)
    val category = _category.asStateFlow()

    val _editCategoryUiState = MutableStateFlow(EditCategoryUiState())
    val editCategoryUiState = _editCategoryUiState.asStateFlow()

   val words = repository.words.stateIn(
       scope = viewModelScope,
       started = SharingStarted.Eagerly,
       initialValue = emptyList()
   )

    val categoryList = repository.categories.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    val _navigationEvent = MutableSharedFlow<EditCategoryEvents>()
    val navigationEvent = _navigationEvent.asSharedFlow()



    init {
        viewModelScope.launch {
            categoryList.collect { list ->
                val currentCategory = list.find { it.id == categoryId }
                _category.value = currentCategory

                _editCategoryUiState.update {
                    it.copy(
                        category = currentCategory?.categoryName ?: "",
                        color = currentCategory?.color ?: Color.Unspecified.toArgb()
                    )
                }
            }
        }



    }

    fun onAction(action: EditCategoryActions) {
        when(action) {
            is EditCategoryActions.OnColorChange -> onColorChange(action.newColor)
            is EditCategoryActions.OnEdit -> onEdit(action.categoryName)
            is EditCategoryActions.OnNameChange -> onNameChange(action.newName)
            EditCategoryActions.ShowColorPicker -> showColorPicker()
        }
    }

   private fun onNameChange(newName: String) {
        _editCategoryUiState.update {
            it.copy(category = newName,
                error = null)
        }
    }

   private fun onColorChange(newColor: Color) {
        _editCategoryUiState.update {
            it.copy(
                color = newColor.toArgb()
            )
        }
    }

    private fun validation(categoryName: String): ErrorTypes? {
        val categories = categoryList.value
        return when {
            categoryName.isBlank() -> ErrorTypes.isBlank
            categories.any{it.categoryName.equals(categoryName, ignoreCase = true) && it.id != categoryId}
                -> ErrorTypes.categoryIsExist
            else -> null
        }
    }

  private  fun onEdit(categoryName: String){
        val error = validation(categoryName)

        if (error != null) {
            _editCategoryUiState.update {
                it.copy(
                    error = error
                )
            }
            return
        }

        val currentCategory = _category.value

        //Find all words that has the currentCategory name in its category list
        val wordsWithCategory = words.value.filter { it.category.contains(currentCategory!!.categoryName)}

        viewModelScope.launch {
            //Edit the category name in the database
            repository.addCategory(
                currentCategory!!.copy(
                    categoryName = categoryName,
                    color = editCategoryUiState.value.color
                )
            )

            //Find all words with the current category and then delete it and add the new category name
            wordsWithCategory.forEach { word->
                repository.updateWordWithCategory(word.copy(
                    category = word.category - currentCategory.categoryName + categoryName
                ))
            }


            //Update in Firebase
            repository.updateCategoryOnFirebase(
                currentCategory.copy(
                    categoryName = categoryName,
                    color = editCategoryUiState.value.color,
                ),
                currentCategory.language
            )

            //onSuccess
            _navigationEvent.emit(EditCategoryEvents.Navigate)
        }



    }

  private  fun showColorPicker() {
        _editCategoryUiState.update {
            it.copy(
                showColorPicker = !it.showColorPicker
            )
        }
    }



}