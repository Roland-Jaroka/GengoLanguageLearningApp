package com.example.gengolearning.ui.features.autchentication.login




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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gengolearning.model.utils.AnalyticsHelper
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.BlueGray
import com.example.gengolearning.ui.theme.White
import com.example.gengolearning.ui.components.ErrorModal
import com.example.gengolearning.ui.components.MyAppButton
import com.example.gengolearning.ui.features.navigation.Route
import com.gengolearning.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginUi(navController: NavController,
            viewModel: LoginViewModel = hiltViewModel()
) {

    val email= viewModel.email
    val password= viewModel.password
    val emailError = viewModel.emailError?.let { id-> stringResource(id) }
    val passwordError = viewModel.passwordError?.let { id-> stringResource(id) }

    var visible by remember { mutableStateOf(false) }

    val loginState by viewModel.loginState.collectAsState()

    val sheetState = rememberModalBottomSheetState()

    val isPasswordVisible = viewModel.isPasswordVisible




    LaunchedEffect(Unit) {
        visible = true

    }

    LaunchedEffect(loginState) {
       if (loginState is LoginStates.Success){
           navController.navigate(Route.MainLanguageSelector){
               popUpTo(Route.MainLanguageSelector){
                   inclusive= true
               }
           }
       }
    }

    Column (modifier= Modifier
        .fillMaxSize()
        .background(White)){
        Box {
            this@Column.AnimatedVisibility(
                visible = visible,
                enter = slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(
                        durationMillis = 1000,
                        delayMillis = 100
                    )
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
            ) {
                this@Column.AnimatedVisibility(
                    visible = visible,
                    enter = slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(
                            durationMillis = 1000,
                            delayMillis = 100
                        )
                    )
                ) {
                    Image(
                        painter = painterResource(R.drawable.globe_improved),
                        contentDescription = null
                    )
                }
            }
        }

        Box(modifier = Modifier
            .align(Alignment.CenterHorizontally)) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
            ) {

                Text(
                    text = "Gen-Go\n Learning",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    fontSize = 40.sp,
                    fontFamily = FontFamily.Cursive,
                    fontWeight = FontWeight.W800,
                    lineHeight = 50.sp
                )

                OutlinedTextField(
                    value = email,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp, top = 5.dp),
                    onValueChange = {
                        viewModel.onEmailChange(it)
                    },
                    label = { Text(stringResource(R.string.email_hint)) },
                    isError = emailError != null,
                    supportingText = {
                        if (emailError != null) {
                            Text(
                                text = "*$emailError",
                                color = Color.Red
                            )
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null
                        )
                    }
                )


                OutlinedTextField(
                    value = password,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp),
                    onValueChange = {
                        viewModel.onPasswordChange(it)

                    },
                    label = { Text(stringResource(R.string.password_hint)) },
                    isError = passwordError != null,
                    supportingText = {
                        if (passwordError != null) {
                            Text(
                                text = "*$passwordError",
                                color = Color.Red,
                            )
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        AnimatedVisibility(
                            visible = password.isNotBlank()
                        ) {

                            Icon(
                                painter =  if (isPasswordVisible) painterResource(R.drawable.show)
                                else painterResource(R.drawable.hide) ,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable (
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ){
                                        viewModel.setPasswordVisibility()
                                    }
                            )
                        }
                    }
                )



                MyAppButton(
                    modifier=Modifier
                        .padding(start = 18.dp, end = 18.dp, top = 2.dp),
                    text= stringResource(R.string.Login_button),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = White,
                        containerColor = BgBlue
                    ),
                    onClick = {

                           viewModel.login(email, password)


                    },
                    isLoading = loginState is LoginStates.Loading

                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.forgotpassword_button),
                        modifier = Modifier
                            .padding(top = 15.dp, start = 20.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                navController.navigate(Route.ForgotPassword)

                                AnalyticsHelper.logEvent("forgot_password_click")

                            },
                        fontSize = 20.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Medium,
                        color = BlueGray
                    )


                    Text(
                        text = stringResource(R.string.sign_up_button),
                        modifier = Modifier
                            .padding(top = 15.dp, end = 30.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                navController.navigate(Route.SignUp)
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
            text = stringResource((loginState as LoginStates.Error).error.toMessageRes())

        )
    }

}