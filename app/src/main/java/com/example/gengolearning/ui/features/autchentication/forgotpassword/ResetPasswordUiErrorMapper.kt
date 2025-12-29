package com.example.gengolearning.ui.features.autchentication.forgotpassword

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.gengolearning.app.R

@Composable
fun ResetPasswordError.mapErrorToMessageRes(): String {
    return stringResource( when (this){
        ResetPasswordError.InvalidUser -> R.string.user_not_found
        ResetPasswordError.NoInternet -> R.string.no_internet
        ResetPasswordError.UnknownError -> R.string.login_unkown_error
    }
    )

}