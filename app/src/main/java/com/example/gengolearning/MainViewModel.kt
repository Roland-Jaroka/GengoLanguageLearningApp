package com.example.gengolearning

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.model.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
): ViewModel() {


    init {
        loadMainLanguage()
    }
    private fun  loadMainLanguage(){

        viewModelScope.launch {
            userSettingsRepository.loadMainLanguage()
        }
        println("Main language loaded from Dashboard")
    }
}