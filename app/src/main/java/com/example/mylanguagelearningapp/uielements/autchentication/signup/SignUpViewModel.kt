package com.example.mylanguagelearningapp.uielements.autchentication.signup

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mylanguagelearningapp.model.results.SignupResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

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

    suspend fun createAccount(name: String, email: String, password: String, confirmPassword: String): SignupResult {

        val auth= FirebaseAuth.getInstance()
        val db= FirebaseFirestore.getInstance()
        //Blank:
        if (name.isBlank()) return SignupResult.BlankName
        if (email.isBlank()) return SignupResult.BlankEmail
        if (password.isBlank()) return SignupResult.BlankPassword
        if (confirmPassword.isBlank()) return SignupResult.BlankConfirmPassword

        //Invalid:
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return SignupResult.InvalidEmail
        if (password.length < 8) return SignupResult.InvalidPassword
        if (password != confirmPassword) return SignupResult.PasswordMismatch

        try {
           val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return SignupResult.UnknownError("Unknown Error")

            db.collection("users").document(uid)
                .set(mapOf("name" to name)).await()

            auth.signOut()

            return SignupResult.Success

        } catch (e: Exception) {

             if (e.message?.contains("already in use") == true) {
                 println("Error: ${e.message}")
                 return  SignupResult.Error
            } else {
                 return SignupResult.UnknownError(e.message ?: "Unknown Error")
            }

        }


    }

}