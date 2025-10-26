package com.example.mylanguagelearningapp.uielements.autchentication.signup

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mylanguagelearningapp.model.results.FieldValidationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

sealed class SignUpState{
    object Idle: SignUpState()
    object Loading: SignUpState()
    object Success: SignUpState()
    data class Error(val message: String): SignUpState()
}
class SignUpViewModel: ViewModel() {




    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

   fun onNameChange(newName: String) {
        name = newName
    }

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        confirmPassword = newConfirmPassword

    }
     private val _signUpState= MutableStateFlow<SignUpState>(SignUpState.Idle)
     val signUpState= _signUpState.asStateFlow()

    fun fieldValidation(name: String, email: String, password: String, confirmPassword: String) : FieldValidationResult{

        if (name.isBlank()) return FieldValidationResult.BlankName
        if (email.isBlank()) return FieldValidationResult.BlankEmail
        if (password.isBlank()) return FieldValidationResult.BlankPassword
        if (confirmPassword.isBlank()) return FieldValidationResult.BlankConfirmPassword

        //Invalid:
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return FieldValidationResult.InvalidEmail
        if (password.length < 8) return FieldValidationResult.InvalidPassword
        if (password != confirmPassword) return FieldValidationResult.PasswordMismatch
        return FieldValidationResult.Success
    }

    suspend fun createAccount(name: String, email: String, password: String) {

        val auth= FirebaseAuth.getInstance()
        val db= FirebaseFirestore.getInstance()

         _signUpState.value = SignUpState.Loading

        try {
           val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: ""


            db.collection("users").document(uid)
                .set(mapOf("name" to name)).await()

            auth.signOut()

            _signUpState.value = SignUpState.Success


        } catch (e: Exception) {

            _signUpState.value = SignUpState.Error(e.message ?: "Unknown Error")

             if (e.message?.contains("already in use") == true) {
                 println("Error: ${e.message}")
                 _signUpState.value = SignUpState.Error("Email already in use")
            } else {
                _signUpState.value = SignUpState.Error(e.message ?: "Unknown Error")
            }

        }


    }

    fun resetState(){
        _signUpState.value= SignUpState.Idle
    }

}