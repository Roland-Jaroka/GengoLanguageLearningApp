package com.example.mylanguagelearningapp.uielements.autchentication.login

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mylanguagelearningapp.model.results.FieldValidationResult
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

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password= newPassword
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
        _loginState.value= LoginStates.Loading
        try{
            auth.signInWithEmailAndPassword(email, password).await()

            _loginState.value= LoginStates.Success

        } catch (e: Exception){
            _loginState.value= LoginStates.Error(e.message ?: "Unknown Error")
        }
    }

    fun resetState(){
        _loginState.value= LoginStates.Idle

    }

    }