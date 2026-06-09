package com.example.gengolearning.ui.features.autchentication.login

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.model.utils.AnalyticsHelper
import com.example.gengolearning.data.repositories.UserSettingsRepository
import com.example.gengolearning.model.results.FieldValidationResult
import com.gengolearning.app.R
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class LoginStates{
    object Idle: LoginStates()
    object Loading: LoginStates()
    object Success: LoginStates()
    data class Error(val error: LoginError): LoginStates()
}
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
): ViewModel() {



    val auth= FirebaseAuth.getInstance()
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    var emailError by mutableStateOf<Int?>(null)
        private set
    var passwordError by mutableStateOf<Int?>(null)
        private set

    var isPasswordVisible by mutableStateOf(false)
        private set

    var isLanguageSelection by mutableStateOf(false)
        private set

    fun loginActions(action: LoginActions) {
        when (action) {
            LoginActions.OnForgotPassword -> TODO()
            LoginActions.OnGoogleSignUp -> TODO()
            LoginActions.OnLanguage -> onLanguage()
            is LoginActions.OnLogin -> {
                login(action.email, action.password)
            }
            LoginActions.OnSignUp -> TODO()
            is LoginActions.OnEmailChange ->
                onEmailChange(action.newInput)
            is LoginActions.OnPasswordChange -> onPasswordChange(action.newInput)
            LoginActions.OnRevealPassword -> setPasswordVisibility()
            LoginActions.OnResetState -> resetState()
        }
    }


 private   fun onEmailChange(newEmail: String) {
        email = newEmail
        emailError = null
    }

   private fun onPasswordChange(newPassword: String) {
        password= newPassword
        passwordError = null
    }


   private fun setPasswordVisibility(){
        isPasswordVisible = !isPasswordVisible
    }

    private val _loginState= MutableStateFlow<LoginStates>(LoginStates.Idle)
    val loginState= _loginState.asStateFlow()

   private fun fieldValidation(email: String, password: String): FieldValidationResult {
        if (email.isBlank()) return FieldValidationResult.BlankEmail
        if (password.isBlank()) return FieldValidationResult.BlankPassword
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return FieldValidationResult.InvalidEmail
        return FieldValidationResult.Success
    }

   private  fun login(email:String, password: String) {
      val auth= FirebaseAuth.getInstance()
        val result = fieldValidation(email, password)

        when(result){
            is FieldValidationResult.BlankEmail -> emailError = R.string.blank_email
            is FieldValidationResult.BlankPassword -> passwordError = R.string.blank_password
            is FieldValidationResult.InvalidEmail -> emailError = R.string.email_input_error
            is FieldValidationResult.Success -> {
                _loginState.value = LoginStates.Loading
                viewModelScope.launch {
                try {
                    auth.signInWithEmailAndPassword(email.trim(), password.trim()).await()

                    userSettingsRepository.getUserName()


                    _loginState.value = LoginStates.Success

                    AnalyticsHelper.logEvent("login_success")

                } catch (e: Exception) {

                    val mappedError = LoginErrorMapper.map(e)
                    _loginState.value = LoginStates.Error(mappedError)
                }
            }
            }
            else -> Unit
        }

    }

  private fun resetState(){
        _loginState.value= LoginStates.Idle

    }

    private fun onLanguage() {
        isLanguageSelection = !isLanguageSelection
    }

    }