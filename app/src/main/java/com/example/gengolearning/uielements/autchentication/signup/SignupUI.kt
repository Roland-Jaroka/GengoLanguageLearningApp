package com.example.gengolearning.uielements.autchentication.signup

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.White
import com.example.gengolearning.uielements.uimodels.ErrorModal
import com.example.gengolearning.uielements.uimodels.MyAppButton
import com.example.gengolearning.uielements.uimodels.TextButton
import com.gengolearning.app.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpUi(navController: NavController,
             viewModel: SignUpViewModel= viewModel()) {


    val name = viewModel.name
    val email = viewModel.email
    val password = viewModel.password
    val confirmPassword = viewModel.confirmPassword

    val nameError = viewModel.nameError?.let { id-> stringResource(id) }
    val emailError = viewModel.emailError?.let { id-> stringResource(id) }
    val passwordError = viewModel.passwordError?.let { id-> stringResource(id) }
    val confirmPasswordError =  viewModel.confirmPasswordError?.let { id-> stringResource(id) }

    val sheetState= rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var isSheetOpen by rememberSaveable { mutableStateOf(false) }
    val signUpState by viewModel.signUpState.collectAsState()




    val scope = rememberCoroutineScope()
    val context= LocalContext.current

    LaunchedEffect(Unit) {
        isSheetOpen = true
    }

    LaunchedEffect(signUpState) {
        if (signUpState is SignUpState.Success){
            navController.navigate("login") {
                popUpTo("signup") {
                    inclusive = true
                }
            }
            Toast.makeText(context,"Account created successfully", Toast.LENGTH_SHORT).show()
        }
    }

Box(modifier = Modifier
    .fillMaxSize()
    .background(White)) {
    Column(modifier = Modifier.align(Alignment.Center)) {
        Text(
            text = stringResource(R.string.sign_up_button),
            fontSize = 30.sp,
            color = Blue,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = stringResource(R.string.create_account_info),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = {viewModel.onNameChange(it)},
            label = { Text(stringResource(R.string.name_field_hint)) },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = 30.dp, end = 30.dp, top = 20.dp)
                .fillMaxWidth(),
            singleLine = true,
            isError = nameError != null,
            supportingText = {
                if (nameError != null) {
                    Text(
                        text = "*$nameError",
                        color = Color.Red
                    )
                }
            },
            shape = RoundedCornerShape(20.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = Blue
                )
            }
        )





        OutlinedTextField(
            value = email,
            onValueChange = {viewModel.onEmailChange(it) },
            label = { Text(stringResource(R.string.email_hint)) },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = 30.dp, end = 30.dp)
                .fillMaxWidth(),
            singleLine = true,
            isError = emailError!= null,
            supportingText = {
                if (emailError != null) {
                    Text(
                        text = "*$emailError",
                        color = Color.Red
                    )
                }
            },
            shape = RoundedCornerShape(20.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = Blue
                )
            }
        )




        OutlinedTextField(
            value = password,
            onValueChange = {viewModel.onPasswordChange(it) },
            label = { Text(stringResource(R.string.password_hint))},
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = 30.dp, end = 30.dp)
                .fillMaxWidth(),
            singleLine = true,
            isError = passwordError!= null,
            supportingText = {
                if (passwordError != null) {
                    Text(
                        text = "*$passwordError",
                        color = Color.Red
                    )
                }
            },
            shape = RoundedCornerShape(20.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Blue
                )
            }
        )



        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {viewModel.onConfirmPasswordChange(it)},
            label = { Text(stringResource(R.string.confirm_password_hint))},
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = 30.dp, end = 30.dp)
                .fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(20.dp),
            isError = confirmPasswordError!= null,
            supportingText = {
                if (confirmPasswordError != null) {
                    Text(
                        text = "*$confirmPasswordError",
                        color = Color.Red
                    )
                }
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Blue
                )
            }
        )



        MyAppButton(
            modifier=Modifier
                .padding(start = 18.dp, end = 18.dp, top = 2.dp),
            text= stringResource(R.string.sign_up_button),
            colors = ButtonDefaults.buttonColors(
                contentColor = White,
                containerColor = Blue
            ),
            onClick = {

                scope.launch {
                    viewModel.createAccount(name, email, password, confirmPassword)
                }
            },
            isLoading = signUpState is SignUpState.Loading

        )

        TextButton(
            onClick = {
                navController.popBackStack()
            },
            text = stringResource(R.string.backtologin_button)
        )
    }

    if (isSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { isSheetOpen = false },
            containerColor = White
        ) {
                Column {
                    Image(
                        painter = painterResource(R.drawable.alert_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(90.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Text(text = stringResource(R.string.be_careful_sign),
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold)
                    Text(text = stringResource(R.string.sing_up_modal_body),
                        modifier = Modifier.padding(start = 30.dp, end = 30.dp, top = 10.dp),
                        fontSize = 18.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)

                    MyAppButton(
                        onClick = {
                            isSheetOpen = false
                        },
                        text = stringResource(R.string.okay_button),
                        colors = ButtonDefaults.buttonColors(BgBlue),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                }

        }
    }
}
    if (signUpState is SignUpState.Error){
        ErrorModal(
            sheetState= sheetState,
            onClick = {
                viewModel.resetState()
            },
            text= (signUpState as SignUpState.Error).message
        )
    }
}
