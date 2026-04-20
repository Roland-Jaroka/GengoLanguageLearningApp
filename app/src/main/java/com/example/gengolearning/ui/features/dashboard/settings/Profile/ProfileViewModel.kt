package com.example.gengolearning.ui.features.dashboard.settings.Profile

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.data.repositories.LanguageGrammar
import com.example.gengolearning.data.repositories.LanguageWords
import com.example.gengolearning.data.repositories.UserSettingsRepository
import com.example.gengolearning.model.appmodels.Grammar
import com.example.gengolearning.model.appmodels.ProfilePicture
import com.example.gengolearning.ui.features.dashboard.settings.Profile.EmailEditScreen.ProfileNameEditFieldValidationResults
import com.gengolearning.app.R
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.io.ByteArrayOutputStream

data class ProfileState(
    val profileName: String = "",
    val wordCount: Int = 0,
    val languageCount: Int = 0
)


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: LanguageWords,
    private val grammarRepository: LanguageGrammar,
    private val userSettingsRepository: UserSettingsRepository,
    private val app: Application
): ViewModel() {

    val wordsList = repository.words.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    private val _grammar = MutableStateFlow<List<Grammar>>(emptyList())
    val grammar = _grammar.asStateFlow()


    val profileState : StateFlow<ProfileState> = combine(
        userSettingsRepository.username,
        repository.getWordCount(),
        repository.getLanguageCount()
    ) {username, wordCount, languageCount->
        ProfileState(
            profileName = username,
            wordCount = wordCount,
            languageCount = languageCount
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProfileState()
    )


    val image = userSettingsRepository.profileImage.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )




    val currentLanguage = userSettingsRepository.selectedLanguage


  private  val _profileNameEditState = MutableStateFlow(
        ProfileNameEditState()
    )
    val profileNameEditState = _profileNameEditState.asStateFlow()

    init {
        grammarRepository.grammar.onEach { list->
            _grammar.value = list
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            profileState.collect { profileState ->
                _profileNameEditState.update {
                    it.copy(
                        name = profileState.profileName
                    )
                }
            }
        }


    }


    fun loadImageFromGallery(uri: Uri) {
        viewModelScope.launch {

            val inputStream = app.contentResolver.openInputStream(uri)
            inputStream?.use { stream ->

                //Decoding the image
                val bitmap = BitmapFactory.decodeStream(stream)

                //Resize
                val resizedBitmap = Bitmap.createScaledBitmap(bitmap,
                    256,
                    256,
                    true)

                //Compressing the image
                val outputStream = ByteArrayOutputStream()
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)


                val bytes = outputStream.toByteArray()

                userSettingsRepository.setProfilePicture(
                    ProfilePicture(
                        image = bytes,
                        id = 1
                    )
                )

            }


        }
    }

    fun deletePicture(picture: ProfilePicture) {

        viewModelScope.launch {
            userSettingsRepository.deleteProfilePicture(picture)
        }
    }





    fun onNameInputChange(name: String) {
        _profileNameEditState.update {
            it.copy(
                name = name,
                fieldError = false,
                fieldValidationMessage = null
            )
        }
    }
    fun resetEmailEditState () {
        _profileNameEditState.update {
            it.copy(
                success = EditState.Idle
            )
        }
    }

    fun fieldValidation(name: String): ProfileNameEditFieldValidationResults {
          val oldvalue = profileState.value.profileName
      return  when {
            (name.length > 10) -> {ProfileNameEditFieldValidationResults.TooLong}
          (name.isEmpty()) -> {ProfileNameEditFieldValidationResults.Empty}
            (name == oldvalue) -> {ProfileNameEditFieldValidationResults.SameAsBefore}

            else -> {ProfileNameEditFieldValidationResults.Success}
        }
    }

    fun changeProfileName(profileName: String) {

        val fieldValidation = fieldValidation(profileName)

        when (fieldValidation) {
            is ProfileNameEditFieldValidationResults.Empty -> {
                _profileNameEditState.update {
                    it.copy(
                        fieldError = true,
                        fieldValidationMessage = R.string.profile_name_input_empty
                    )
                }
            }
            is ProfileNameEditFieldValidationResults.TooLong -> {
                _profileNameEditState.update {
                    it.copy(
                        fieldError = true,
                        fieldValidationMessage = R.string.profile_name_input_too_long
                    )
                }
            }
            is ProfileNameEditFieldValidationResults.SameAsBefore -> {
                _profileNameEditState.update {
                    it.copy(
                        fieldError = true,
                        fieldValidationMessage = R.string.profile_name_input_same_input
                    )
                }
            }
            is ProfileNameEditFieldValidationResults.Success -> {

                _profileNameEditState.update {
                    it.copy(
                        isLoading = true
                    )
                }

                viewModelScope.launch {

                    try {
                        withTimeout(5000) {
                            userSettingsRepository.editUserName(profileName)
                        }
                        _profileNameEditState.update {
                            it.copy(
                                isLoading = false,
                                success = EditState.Success
                            )
                        }

                    } catch (e: Exception) {
                        _profileNameEditState.update {
                            it.copy(
                                isLoading = false,
                                success = EditState.Failure
                            )
                        }
                    }
                }
            }
        }
    }
}

