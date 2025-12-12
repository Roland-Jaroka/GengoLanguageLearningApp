package com.example.gengolearning.uielements.autchentication.forgotpassword

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.gengolearning.model.results.ResetPasswordResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class ForgotPasswordViewModel: ViewModel() {
    val auth = FirebaseAuth.getInstance()
    var email by mutableStateOf("")

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }


   suspend fun onResetPassword(email: String): ResetPasswordResult {
        if (email.isBlank()) {
            return ResetPasswordResult.BlankEmail
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ResetPasswordResult.InvalidEmail
        }
        try {
            auth.sendPasswordResetEmail(email).await()
            return ResetPasswordResult.Success

        } catch (e:Exception) {
            return ResetPasswordResult.Error(e.message?: "Unknown Error")

        }
    }

}