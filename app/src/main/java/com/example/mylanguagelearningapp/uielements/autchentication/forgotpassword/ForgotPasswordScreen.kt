package com.example.mylanguagelearningapp.uielements.autchentication.forgotpassword

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mylanguagelearningapp.model.results.ResetPasswordResult
import com.example.mylanguagelearningapp.ui.theme.Blue
import com.example.mylanguagelearningapp.ui.theme.White
import com.example.mylanguagelearningapp.uielements.uimodels.MyAppButton
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScr(viewModel: ForgotPasswordViewModel = viewModel(),
                      navController: NavController) {

    val email = viewModel.email
    val scope = rememberCoroutineScope()
    val context= LocalContext.current

    var emailError by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(White),
    ) {
        Column(modifier = Modifier.align(Alignment.Center)) {

                   Text(
                       text = "Forgot your password?",
                       fontSize = 30.sp,
                       color = Blue,
                       modifier = Modifier
                           .align(Alignment.CenterHorizontally)
                   )
                   Text(
                       text = "Enter your email to reset your password",
                       modifier = Modifier
                           .align(Alignment.CenterHorizontally)
                           .padding(top = 10.dp)
                   )

                   OutlinedTextField(
                       value = email,
                       onValueChange = {
                           viewModel.onEmailChange(newEmail = it)
                           emailError = null
                       },
                       label = { Text("Email") },
                       modifier = Modifier
                           .align(Alignment.CenterHorizontally)
                           .padding(start = 30.dp, end = 30.dp, top = 20.dp)
                           .fillMaxWidth(),
                       singleLine = true,
                       shape = RoundedCornerShape(20.dp),
                       isError = emailError != null,
                       leadingIcon = {
                           Icon(
                               imageVector = Icons.Default.Email,
                               contentDescription = null,
                               tint = Blue
                           )
                       }
                   )

                   if (emailError != null) {
                       Text(
                           text = "*$emailError",
                           color = Color.Red,
                           modifier = Modifier.padding(start = 30.dp, top = 5.dp)
                       )

                   }

            MyAppButton(
                modifier=Modifier
                    .padding(start = 18.dp, end = 18.dp, top = 2.dp),
                text= "Reset Password",
                colors = ButtonDefaults.buttonColors(
                    contentColor = White,
                    containerColor = Blue
                ),
                enabled = if (email.isBlank()) false else true,
                onClick = {
                    scope.launch {

                        val result = viewModel.onResetPassword(email)

                        when (result) {
                            is ResetPasswordResult.Success -> {
                                navController.navigate("login")

                            }

                            is ResetPasswordResult.BlankEmail -> {
                                emailError = "Email cannot be blank"
                            }

                            is ResetPasswordResult.InvalidEmail -> {
                                emailError = "Invalid Email"
                            }

                            is ResetPasswordResult.Error -> {
                                Toast.makeText(
                                    context,
                                    result.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                }


            )

            Text(
                text = "Back to login",
                fontSize = 15.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 10.dp, bottom = 20.dp, start = 20.dp, end = 20.dp)
                    .clickable(indication = null,
                        interactionSource = remember{ MutableInteractionSource() }) {
                        navController.navigate("login")
                    },
                color = Blue)



        }

    }
}