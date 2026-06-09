package com.example.gengolearning.model.appmodels

import androidx.annotation.StringRes

data class ErrorModalText(
    @StringRes val title: Int,
    @StringRes val text: Int,
    @StringRes  val buttonText: Int
)
