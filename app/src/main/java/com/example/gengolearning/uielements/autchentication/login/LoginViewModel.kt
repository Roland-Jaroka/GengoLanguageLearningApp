package com.example.gengolearning.uielements.autchentication.login

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.gengolearning.model.AnalyticsHelper
import com.example.gengolearning.model.results.FieldValidationResult
import com.gengolearning.app.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

sealed class LoginStates{
    object Idle: LoginStates()
    object Loading: LoginStates()
    object Success: LoginStates()
    data class Error(val message: String): LoginStates()
}
class LoginViewModel: ViewModel() {



    val auth= FirebaseAuth.getInstance()
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    var emailError by mutableStateOf<Int?>(null)
        private set
    var passwordError by mutableStateOf<Int?>(null)
        private set




    fun onEmailChange(newEmail: String) {
        email = newEmail
        emailError = null
    }

    fun onPasswordChange(newPassword: String) {
        password= newPassword
        passwordError = null
    }

    private val _loginState= MutableStateFlow<LoginStates>(LoginStates.Idle)
    val loginState= _loginState.asStateFlow()

    fun fieldValidation(email: String, password: String): FieldValidationResult {
        if (email.isBlank()) return FieldValidationResult.BlankEmail
        if (password.isBlank()) return FieldValidationResult.BlankPassword
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return FieldValidationResult.InvalidEmail
        return FieldValidationResult.Success
    }

    suspend fun login(email:String, password: String) {
      val auth= FirebaseAuth.getInstance()
        val result = fieldValidation(email, password)

        when(result){
            is FieldValidationResult.BlankEmail -> emailError = R.string.blank_email
            is FieldValidationResult.BlankPassword -> passwordError = R.string.blank_password
            is FieldValidationResult.InvalidEmail -> emailError = R.string.email_input_error
            is FieldValidationResult.Success -> {
                _loginState.value= LoginStates.Loading
                try{
                    auth.signInWithEmailAndPassword(email, password).await()

                    _loginState.value= LoginStates.Success

                    AnalyticsHelper.logEvent("login_success")

                } catch (e: Exception){
                    _loginState.value= LoginStates.Error(e.message ?: "Unknown Error")
                }
            }
            else -> Unit
        }

    }

    fun resetState(){
        _loginState.value= LoginStates.Idle

    }

    }