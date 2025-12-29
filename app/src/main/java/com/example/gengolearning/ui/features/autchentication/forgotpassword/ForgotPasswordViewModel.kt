package com.example.gengolearning.ui.features.autchentication.forgotpassword

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.gengolearning.model.results.ResetPasswordResult
import com.gengolearning.app.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
sealed class uiState {
    object Idle : uiState()
    object Loading : uiState()
    object Success : uiState()
    data class UiError(val emailError: Int) : uiState()
    data class Error (val error: ResetPasswordError): uiState()
}
class ForgotPasswordViewModel: ViewModel() {
    val auth = FirebaseAuth.getInstance()
    var email by mutableStateOf("")

    private val _state = MutableStateFlow<uiState>(uiState.Idle)
    val state = _state.asStateFlow()


    fun onEmailChange(newEmail: String) {
        email = newEmail
        _state.value = uiState.Idle
    }

    private fun fieldValidation(email: String): ResetPasswordResult {
        if (email.isBlank()) {
            return ResetPasswordResult.BlankEmail
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            return ResetPasswordResult.InvalidEmail
        }

        else return ResetPasswordResult.Success
    }

  suspend fun onResetPassword(email: String) {
         val result = fieldValidation(email)

      when (result){
          is ResetPasswordResult.BlankEmail -> _state.value = uiState.UiError(R.string.blank_email)
          is ResetPasswordResult.InvalidEmail -> _state.value =  uiState.UiError(R.string.invalid_email)
          is ResetPasswordResult.Success -> {
              _state.value = uiState.Loading
              try {
                  auth.sendPasswordResetEmail(email).await()
                  _state.value = uiState.Success

              } catch (e: Exception) {

                  val mappedError = ResetPasswordErrorMapper.map(e)
                  _state.value = uiState.Error(mappedError)

              }
          }
          else -> Unit
      }


  }

    fun resetState(){
        _state.value= uiState.Idle
    }

}