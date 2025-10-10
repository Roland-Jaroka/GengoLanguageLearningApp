package com.example.mylanguagelearningapp.uielements.autchentication.signup

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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import com.example.mylanguagelearningapp.model.results.SignupResult
import com.example.mylanguagelearningapp.ui.theme.Blue
import com.example.mylanguagelearningapp.ui.theme.White
import com.example.mylanguagelearningapp.uielements.uimodels.MyAppButton
import kotlinx.coroutines.launch

@Composable
fun SignUpUi(navController: NavController,
             viewModel: SignUpViewModel= viewModel()) {


    val name = viewModel.name
    val email = viewModel.email
    val password = viewModel.password
    val confirmPassword = viewModel.confirmPassword

    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }



    val scope = rememberCoroutineScope()
    val context= LocalContext.current

Box(modifier = Modifier
    .fillMaxSize()
    .background(White)) {
    Column(modifier = Modifier.align(Alignment.Center)) {
        Text(
            text = "Sign Up",
            fontSize = 30.sp,
            color = Blue,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Create an account to sign up",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = {viewModel.onNameChange(it)
                            nameError = null},
            label = { Text("Name") },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = 30.dp, end = 30.dp, top = 20.dp)
                .fillMaxWidth(),
            singleLine = true,
            isError = nameError != null,
            shape = RoundedCornerShape(20.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = Blue
                )
            }
        )

        if (nameError != null) {
            Text(
                text = "*$nameError",
                color = Color.Red,
                modifier = Modifier.padding(start = 30.dp, top = 5.dp)
            )
        }



        OutlinedTextField(
            value = email,
            onValueChange = {viewModel.onEmailChange(it)
                            emailError= null},
            label = { Text("Email") },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = 30.dp, end = 30.dp, top = 5.dp)
                .fillMaxWidth(),
            singleLine = true,
            isError = emailError!= null,
            shape = RoundedCornerShape(20.dp),
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


        OutlinedTextField(
            value = password,
            onValueChange = {viewModel.onPasswordChange(it)
                            passwordError= null},
            label = { Text("Password")},
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = 30.dp, end = 30.dp, top = 5.dp)
                .fillMaxWidth(),
            singleLine = true,
            isError = passwordError!= null,
            shape = RoundedCornerShape(20.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Blue
                )
            }
        )

        if (passwordError != null) {
            Text(
                text = "*$passwordError",
                color = Color.Red,
                modifier = Modifier.padding(start = 30.dp, top = 5.dp)
            )
        }

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {viewModel.onConfirmPasswordChange(it)
                            confirmPasswordError = null},
            label = { Text("Confirm password")},
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = 30.dp, end = 30.dp, top = 5.dp)
                .fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(20.dp),
            isError = confirmPasswordError!= null,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Blue
                )
            }
        )

        if (confirmPasswordError != null) {
            Text(
                text = "*$confirmPasswordError",
                color = Color.Red,
                modifier = Modifier.padding(start = 30.dp, top = 5.dp)
            )
        }

        MyAppButton(
            modifier=Modifier
                .padding(start = 18.dp, end = 18.dp, top = 2.dp),
            text= "Sign Up",
            colors = ButtonDefaults.buttonColors(
                contentColor = White,
                containerColor = Blue
            ),
            onClick = {

                scope.launch {
                    val result= viewModel.createAccount(name, email, password, confirmPassword)
                    when (result){

                        //Success:
                        is SignupResult.Success -> {navController.navigate("login")
                            Toast.makeText(context,"Account created successfully", Toast.LENGTH_SHORT).show()}

                        //Blank:
                        is SignupResult.BlankName -> nameError= "Name cannot be blank"
                        is SignupResult.BlankEmail -> emailError= "Email cannot be blank"
                        is SignupResult.BlankPassword -> passwordError= "Password cannot be blank"
                        is SignupResult.BlankConfirmPassword -> confirmPasswordError= "Confirm password cannot be blank"

                        //Invalid:
                        is SignupResult.InvalidEmail -> emailError= "Invalid email"
                        is SignupResult.InvalidPassword -> passwordError = "Password has to be at least 8 characters"
                        is SignupResult.PasswordMismatch -> confirmPasswordError = "Passwords do not match"

                        //Error:
                        is SignupResult.Error -> emailError= "This email address is already in use"
                        is SignupResult.UnknownError -> Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()

                    }
                }


            }

        )

        Text(
            text = "Back to login",
            fontSize = 15.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(20.dp)
                .clickable(indication = null,
                    interactionSource = remember{ MutableInteractionSource() }) {
                    navController.navigate("login")
                },
            color = Blue)
    }
}
}
