package com.example.gengolearning.ui.features.dashboard.home.apiwords

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.data.repositories.LanguageWords
import com.example.gengolearning.model.AppSettingsPreferences
import com.example.gengolearning.model.appmodels.ErrorModalText
import com.example.gengolearning.model.errors.NetworkError
import com.example.gengolearning.model.results.Response
import com.gengolearning.app.R
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@HiltViewModel
class ApiWordsViewModel @Inject constructor(
    private val repository : LanguageWords
): ViewModel() {



    private val _uiState = MutableStateFlow(ApiWordsUiState())
    val uiState = _uiState.asStateFlow()



    init {
        loadWordsFromApi()
    }

    fun onAction(action: ApiWordsActions) {
        when (action) {
            is ApiWordsActions.OnLoadWords -> loadWordsFromApi(searchKey = action.searchKey)
            ApiWordsActions.OnResetError -> resetError()
            is ApiWordsActions.OnSearchInput -> onSearchInput(input = action.input)
           is ApiWordsActions.OnSetTutorial -> setTutorial(context = action.context)
            is ApiWordsActions.OnSetTutorialWithInfo -> setTutorialInfo(action.visible)
        }
    }

    fun tutorialModal(context: Context) = AppSettingsPreferences.showJishoSearchTutorial(context)

  private  fun onSearchInput(input: String) {
        val maxChar = 20
        if (input.length > maxChar) return

        _uiState.update {
            it.copy(
                searchInput = input
            )
        }
    }

   private fun loadWordsFromApi(searchKey: String = "house") {

            viewModelScope.launch {
                _uiState.update {
                    it.copy(
                        isLoading = true
                    )
                }
                val data = repository.getWordsFromApi(searchKey)

                   _uiState.update {

                       when (data) {

                           is Response.Success -> {
                               it.copy(
                                   isLoading = false,
                                   wordList = data.data
                               )

                           }

                           is Response.Error -> {
                               when (data.error) {
                                   NetworkError.BasicNetworkError.NO_INTERNET -> {
                                       it.copy(
                                           isLoading = false,
                                           error = true,
                                           modalText = ErrorModalText(
                                               text = R.string.common_error_internet_description,
                                               title = R.string.common_error_internet_title,
                                               buttonText = R.string.common_error_internet_button
                                           )
                                       )

                                   }

                                   NetworkError.BasicNetworkError.SERVER_DOWN -> {
                                       it.copy(
                                           isLoading = false,
                                           error = true,
                                           modalText = ErrorModalText(
                                               text = R.string.common_error_server,
                                               title = R.string.common_error_internet_title,
                                               buttonText = R.string.common_error_internet_button
                                           )
                                       )

                                   }

                                   NetworkError.BasicNetworkError.RATE_LIMIT_REACHED -> {
                                       it.copy(
                                           isLoading = false,
                                           error = true,
                                           modalText = ErrorModalText(
                                               text = R.string.common_error_internet_title,
                                               title = R.string.common_error_internet_description,
                                               buttonText = R.string.common_error_internet_button
                                           )
                                       )

                                   }

                                   NetworkError.BasicNetworkError.UNKOWN_ERROR -> {
                                       it.copy(
                                           isLoading = false,
                                           error = true,
                                           modalText = ErrorModalText(
                                               text = R.string.common_error_internet_title,
                                               title = R.string.login_unkown_error,
                                               buttonText = R.string.common_error_internet_button
                                           )
                                       )
                                   }
                               }
                           }
                       }
                   }

            }

    }

   private fun setTutorial(context: Context) {
        viewModelScope.launch {
            AppSettingsPreferences.setJishoSearchTutorialShown(context, false)
        }
    }

  private  fun resetError() {
        _uiState.update {
            it.copy(
                error = null,
                modalText = null
            )
        }
    }

    private fun setTutorialInfo(visible: Boolean) {
        _uiState.update {
            it.copy(
                tutorialWithInfo = visible
            )
        }
    }



}