package com.example.gengolearning.ui.features.autchentication.login

import androidx.compose.runtime.Composable
import com.gengolearning.app.R

@Composable
fun LoginError.toMessageRes(): Int {
return when (this) {
    LoginError.InvalidCredentials -> R.string.invalid_creditentals
    LoginError.InvalidUser -> R.string.user_not_found
    LoginError.NoInternet -> R.string.no_internet
    LoginError.UnknownError -> R.string.login_unkown_error
}

}