package com.example.gengolearning.ui.features.dashboard.home.mylist.makeNewCategory

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.data.repositories.LanguageWords
import com.example.gengolearning.data.repositories.UserSettingsRepository
import com.example.gengolearning.model.appmodels.WordCategories
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID


@HiltViewModel
class NewCategoryViewmodel @Inject constructor(
    private val repository: LanguageWords,
    val userSettingsRepository: UserSettingsRepository
): ViewModel() {

    val currentLanguage= userSettingsRepository.language.value


    //Have to start collecting Eagerly cause otherwise it will collect nothing cause it only collects if WhileSubscribed when the
    //Ui collects it
    val categories = repository.categories.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )
  private  val _uiState = MutableStateFlow(CategoryMakerUiState())
    val uiState: StateFlow<CategoryMakerUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent= _uiEvent.asSharedFlow()


    fun onInputChanged(newInput: String) {
        _uiState.update {
            it.copy(
                category = newInput,
                error = null
            )
        }
    }

    fun onColorChanged(newColor: Color) {
        _uiState.update {
            it.copy(
                color = newColor
            )
        }
    }

   private fun validation(category: String): ErrorTypes? {

       val categories = categories.value
       println("categories: ${categories}")
        return when {
            category.isBlank() -> ErrorTypes.isBlank
            categories.any { it.categoryName.equals(category, ignoreCase = true)} -> ErrorTypes.categoryIsExist
            else -> null
        }

    }

    fun onSaveCategory(
        category: String,
        color: Color
    ) {
        val error = validation(category)
        if (error != null) {
            _uiState.update {
                it.copy(
                    error = error
                )
            }
            return
        }


        viewModelScope.launch {
            val id = UUID.randomUUID()
            val newCategory = WordCategories(
                id = id.toString(),
                categoryName = category,
                color = color.toArgb(),
                language = currentLanguage
            )
            repository.addCategory(newCategory) //create locally

            repository.addCategoryToFirebase(newCategory, language = currentLanguage) //create in Firebase

            _uiEvent.emit(UiEvent.CategoryCreated)

        }
    }


}