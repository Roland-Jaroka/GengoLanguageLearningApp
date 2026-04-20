package com.example.gengolearning.ui.features.dashboard.home.apiwords

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.model.AppSettingsPreferences
import com.example.gengolearning.model.appmodels.Words
import com.example.gengolearning.data.repositories.LanguageWords
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.util.network.UnresolvedAddressException
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException
import java.net.ConnectException
import java.net.URLEncoder

data class UiState(
    val isLoading: Boolean = false,
    val wordList: List<Words> = emptyList(),
    val error: String? = null
)
@HiltViewModel
class ApiWordsViewModel @Inject constructor(
    private val repository : LanguageWords
): ViewModel() {



    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    var searchInput by mutableStateOf("")
        private set



    init {
        loadWordsFromApi()
    }

    fun tutorialModal(context: Context) = AppSettingsPreferences.showJishoSearchTutorial(context)

    fun onSearchInput(input: String) {
        val maxChar = 20
        if (input.length > maxChar) return
        searchInput = input
    }

    fun loadWordsFromApi(searchKey: String = "house") {

           val searchKeyEncoding = URLEncoder.encode(searchKey, "UTF-8")


            viewModelScope.launch {


                _uiState.update {
                    it.copy(
                        isLoading = true
                    )
                }

                try {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            wordList = repository.getWordsFromApi(searchKeyEncoding)
                        )
                    }
                }

                catch (e: Throwable) {

                    when (e) {
                        is ConnectException,
                        is IOException,
                        is UnresolvedAddressException    -> {

                            println("Error loading words: no internet")

                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = "no internet"
                                )
                            }
                        }

                        else -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = e.message
                                )
                            }
                            println("Error loading words: ${e.message}")
                        }

                    }
                }
            }

    }

    fun setTutorial(context: Context) {
        viewModelScope.launch {
            AppSettingsPreferences.setJishoSearchTutorialShown(context, false)
        }
    }

    fun resetError() {
        _uiState.update {
            it.copy(
                error = null
            )
        }
    }



}