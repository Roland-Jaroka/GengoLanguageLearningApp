package com.example.gengolearning.ui.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.data.repositories.UserSettingsRepository
import com.example.gengolearning.ui.theme.AppColorTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val repository: UserSettingsRepository
): ViewModel() {

    val theme = repository.theme.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AppColorTheme.BASIC
    )
}