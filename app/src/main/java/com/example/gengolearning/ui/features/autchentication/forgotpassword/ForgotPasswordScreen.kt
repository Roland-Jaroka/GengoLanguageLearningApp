package com.example.gengolearning.ui.features.autchentication.forgotpassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.Red
import com.example.gengolearning.ui.theme.White
import com.example.gengolearning.ui.components.ErrorModal
import com.example.gengolearning.ui.components.MyAppButton
import com.example.gengolearning.ui.components.TextButton
import com.gengolearning.app.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScr(viewModel: ForgotPasswordViewModel = viewModel(),
                      navController: NavController) {

    val email = viewModel.email
    val scope = rememberCoroutineScope()
    val state by viewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(state) {
        if (state is uiState.Success) {
            navController.navigate("login") {
                popUpTo("forgot_password") {
                    inclusive = true
                }
            }

        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(White),
    ) {
        Column(modifier = Modifier.align(Alignment.Center)) {

                   Text(
                       text = stringResource(R.string.forgotpassword_button),
                       fontSize = 30.sp,
                       color = Blue,
                       modifier = Modifier
                           .align(Alignment.CenterHorizontally)
                   )
                   Text(
                       text =stringResource(R.string.forgot_password_info) ,
                       modifier = Modifier
                           .align(Alignment.CenterHorizontally)
                           .padding(top = 10.dp, start = 30.dp, end = 30.dp)
                   )

                   OutlinedTextField(
                       value = email,
                       onValueChange = {
                           viewModel.onEmailChange(newEmail = it)
                       },
                       label = { Text(stringResource(R.string.email_hint)) },
                       modifier = Modifier
                           .align(Alignment.CenterHorizontally)
                           .padding(start = 30.dp, end = 30.dp, top = 20.dp)
                           .fillMaxWidth(),
                       singleLine = true,
                       shape = RoundedCornerShape(20.dp),
                       isError = state is uiState.UiError,
                       leadingIcon = {
                           Icon(
                               imageVector = Icons.Default.Email,
                               contentDescription = null,
                               tint = Blue
                           )
                       },
                       supportingText = {
                           if (state is uiState.UiError) {
                               Text(text = stringResource((state as uiState.UiError).emailError),
                                   color = Red
                               )
                           }
                       }
                   )

            MyAppButton(
                modifier=Modifier
                    .padding(start = 18.dp, end = 18.dp, top = 2.dp),
                text= stringResource(R.string.resetpassword_button),
                colors = ButtonDefaults.buttonColors(
                    contentColor = White,
                    containerColor = Blue
                ),
                enabled = email.isNotBlank(),
                onClick = {
                    scope.launch {
                        viewModel.onResetPassword(email)
                    }

                }


            )

            TextButton(
                onClick = {
                   navController.popBackStack()
                },
                text = stringResource(R.string.backtologin_button)
            )



        }

    }

    if (state is uiState.Error) {
        ErrorModal(
            onClick = {
                   viewModel.resetState()
            },
            text = (state as uiState.Error).error.mapErrorToMessageRes(),
            sheetState = sheetState
        )
    }
}