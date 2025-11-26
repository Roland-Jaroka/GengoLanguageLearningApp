package com.example.gengolearning.uielements.autchentication.signup

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.gengolearning.model.AnalyticsHelper
import com.example.gengolearning.model.results.FieldValidationResult
import com.gengolearning.app.R
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

    var nameError by mutableStateOf<Int?>(null)
        private set
    var emailError by mutableStateOf<Int?>(null)
        private set

    var passwordError by mutableStateOf<Int?>(null)
        private set

    var confirmPasswordError by mutableStateOf<Int?>(null)
        private set


   fun onNameChange(newName: String) {
        name = newName
       nameError = null
    }

    fun onEmailChange(newEmail: String) {
        email = newEmail
        emailError = null
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
        passwordError = null
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        confirmPassword = newConfirmPassword
        confirmPasswordError = null

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

    suspend fun createAccount(name: String, email: String, password: String, confirmPassword: String) {

        val auth= FirebaseAuth.getInstance()
        val db= FirebaseFirestore.getInstance()
        val result = fieldValidation(name, email, password, confirmPassword)

        when (result){

            //Blank:
            is FieldValidationResult.BlankName -> nameError= R.string.name_input_error
            is FieldValidationResult.BlankEmail -> emailError= R.string.blank_email
            is FieldValidationResult.BlankPassword -> passwordError= R.string.blank_password
            is FieldValidationResult.BlankConfirmPassword -> confirmPasswordError= R.string.confirm_password_input_error


            //Invalid:
            is FieldValidationResult.InvalidEmail -> emailError= R.string.email_input_error
            is FieldValidationResult.InvalidPassword -> passwordError = R.string.password_character_size
            is FieldValidationResult.PasswordMismatch -> confirmPasswordError = R.string.confrim_password_match

            //Error:
            is FieldValidationResult.Error -> emailError= R.string.email_already_in_use

            //Success:
            is FieldValidationResult.Success -> {
                _signUpState.value = SignUpState.Loading

                try {
                    val result = auth.createUserWithEmailAndPassword(email, password).await()
                    val uid = result.user?.uid ?: ""


                    db.collection("users").document(uid)
                        .set(mapOf("name" to name)).await()

                    auth.signOut()

                    _signUpState.value = SignUpState.Success

                    AnalyticsHelper.logEvent("signup_success")


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


        }




    }

    fun resetState(){
        _signUpState.value= SignUpState.Idle
    }

}