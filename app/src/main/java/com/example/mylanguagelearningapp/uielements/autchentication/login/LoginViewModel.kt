package com.example.mylanguagelearningapp.uielements.autchentication.login

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mylanguagelearningapp.model.results.LoginResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

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

    suspend fun login(email:String, password: String): LoginResult {
        if (email.isBlank()) return LoginResult.BlankEmail
        if (password.isBlank()) return LoginResult.BlankPassword
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return LoginResult.InvalidEmail
        return try {
            auth.signInWithEmailAndPassword(email,password).await()
            LoginResult.Success
        } catch (e: Exception) {

            if (e.message?.contains("user not found") == true || e.message?.contains("wrong password") == true) LoginResult.Error

            else return LoginResult.UnknownError(e.message ?: "Unknown Error")

        }
    }

    }