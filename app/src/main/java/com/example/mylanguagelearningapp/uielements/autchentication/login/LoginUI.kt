package com.example.mylanguagelearningapp.uielements.autchentication.login



import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mylanguagelearningapp.R
import com.example.mylanguagelearningapp.model.results.FieldValidationResult
import com.example.mylanguagelearningapp.ui.theme.BgBlue
import com.example.mylanguagelearningapp.ui.theme.BlueGray
import com.example.mylanguagelearningapp.ui.theme.White
import com.example.mylanguagelearningapp.uielements.uimodels.ErrorModal
import com.example.mylanguagelearningapp.uielements.uimodels.MyAppButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginUi(navController: NavController,
            viewModel: LoginViewModel = viewModel()
) {

    val email= viewModel.email
    val password= viewModel.password
    val scope = rememberCoroutineScope()
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    var visible by remember { mutableStateOf(false) }

    val loginState by viewModel.loginState.collectAsState()

    val sheetState = rememberModalBottomSheetState()




    LaunchedEffect(Unit) {
        visible = true
        println("Logged in user email ${viewModel.auth.currentUser?.email}")

    }

    LaunchedEffect(loginState) {
       if (loginState is LoginStates.Success){
           navController.navigate("mainLanguageSelector"){
               popUpTo("authentication"){
                   inclusive= true
               }
           }
       }
    }

    Box(modifier= Modifier
        .fillMaxSize()
        .background(White)){

        AnimatedVisibility(
            visible= visible,
            enter = slideInHorizontally(
                initialOffsetX = {fullWidth -> -fullWidth},
                animationSpec = tween(durationMillis = 1000,
                    delayMillis = 100)
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)
                    .clip(RoundedCornerShape(bottomEnd = 300.dp))
                    .background(BgBlue)
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 30.dp)
        ){
        AnimatedVisibility(
            visible= visible,
            enter = slideInHorizontally(
                initialOffsetX = {fullWidth -> fullWidth},
                animationSpec = tween(durationMillis = 1000,
                    delayMillis = 100)
            )
        ) {
            Image(
                painter = painterResource(R.drawable.globe_improved),
                contentDescription = null
                )
        }
             }

        Spacer(modifier = Modifier.height(200.dp))

        Box(modifier = Modifier.align(Alignment.Center)) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
            ) {

               Spacer(modifier = Modifier.height(200.dp))

                Text(
                    text = "   言語\n Learning",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    fontSize = 40.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 50.sp
                )

                OutlinedTextField(
                    value = email,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp, top = 20.dp),
                    onValueChange = {
                        viewModel.onEmailChange(it)
                        emailError = null
                    },
                    label = { Text("Email") },
                    isError = emailError != null,
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp, top = 5.dp),
                    onValueChange = {
                        viewModel.onPasswordChange(it)
                        passwordError = null
                    },
                    label = { Text("Password") },
                    isError = passwordError != null,
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null
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

                MyAppButton(
                    modifier=Modifier
                        .padding(start = 18.dp, end = 18.dp, top = 2.dp),
                    text= "Login",
                    colors = ButtonDefaults.buttonColors(
                        contentColor = White,
                        containerColor = BgBlue
                    ),
                    onClick = {
                        val result = viewModel.fieldValidation(email, password)
                        when(result){
                            is FieldValidationResult.BlankEmail -> emailError = "Email cannot be blank"
                            is FieldValidationResult.BlankPassword -> passwordError = "Password cannot be blank"
                            is FieldValidationResult.InvalidEmail -> emailError = "Invalid email"
                            is FieldValidationResult.Success -> {
                                scope.launch {
                                    viewModel.login(email, password)
                                }
                            }
                            else -> Unit
                        }

                    },
                    isLoading = loginState is LoginStates.Loading

                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Forgot password?",
                        modifier = Modifier
                            .padding(top = 15.dp, start = 20.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                navController.navigate("forgot_password")

                            },
                        fontSize = 20.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Medium,
                        color = BlueGray
                    )


                    Text(
                        text = "Sign Up",
                        modifier = Modifier
                            .padding(top = 15.dp, end = 30.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                navController.navigate("signup")
                            },
                        fontSize = 20.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        color = BlueGray
                    )
                }

            }
        }//Box
    }
    if (loginState is LoginStates.Error) {
        ErrorModal(
            sheetState,
            onClick = {
                        viewModel.resetState()
            },
            text = (loginState as LoginStates.Error).message

        )
    }

}