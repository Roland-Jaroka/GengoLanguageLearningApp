package com.example.gengolearning.ui.features.autchentication.signup

import androidx.compose.runtime.Composable
import com.gengolearning.app.R

@Composable
fun SignUpErrors.toMessageRes(): Int {
    return when (this) {
        SignUpErrors.EmailAlreadyInUse -> R.string.email_already_in_use
        SignUpErrors.MalformedEmail -> R.string.invalid_email
        SignUpErrors.NoInternet -> R.string.no_internet
        SignUpErrors.UnknownError -> R.string.login_unkown_error
        SignUpErrors.WeakPassword -> R.string.weak_password
    }

}