package com.example.gengolearning.uielements.dashboard.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gengolearning.model.AnalyticsHelper
import com.gengolearning.app.R
import com.example.gengolearning.model.UserSettingsRepository
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.Red
import com.example.gengolearning.ui.theme.White
import com.example.gengolearning.uielements.uimodels.AppSettingsAlertDialog
import com.example.gengolearning.uielements.uimodels.InfoModal
import com.example.gengolearning.uielements.uimodels.MyAppButton
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun settingsUi(navController: NavController, viewModel: SettingsViewModel= viewModel()) {
   //TODO settings UI and functions
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val currentLanguage by UserSettingsRepository.selectedLanguage.collectAsState()
    val sheetState= rememberModalBottomSheetState()
    var infoModal by rememberSaveable { mutableStateOf(false) }
    val openAlertDialog = remember{mutableStateOf(false)}

    LaunchedEffect(Unit) {
        println("Language set to: ${UserSettingsRepository.language.value}")
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(White)) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .clip(RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp))
                .background(BgBlue)
        ){
            Text( text = stringResource(R.string.setting_button),
                fontSize = 50.sp,
                modifier = Modifier
                    .padding(top = 30.dp, start = 20.dp)
                    .align(Alignment.Center),
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.Bold,
                color = White)
        }

        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center){



            Card(modifier = Modifier
                .padding(start = 12.dp, end = 12.dp),
                colors = CardDefaults.cardColors(White),
                elevation = CardDefaults.cardElevation(10.dp)){
                Column {

                    SettingItems(
                        icon = {
                            Image(
                                painter= painterResource(R.drawable.profile),
                                contentDescription = "profile",
                                modifier = Modifier.size(80.dp)
                            )
                        },
                        title =stringResource(R.string.profile),
                        arrow = {
                            Image(
                                painter = painterResource(R.drawable.outline_arrow_forward),
                                contentDescription = "arrow")
                        },
                        onClick= {
                            navController.navigate("profile")
                        }

                    )

                    SettingItems(
                        {
                            Box(modifier = Modifier
                                .padding(10.dp)
                                .background(White)
                                .height(65.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(2.dp, BgBlue, RoundedCornerShape(30.dp))
                                ){
                                Image(
                                    painter = painterResource(currentLanguage.flag),
                                    contentDescription = null,
                                    modifier = Modifier.size(70.dp)
                                )
                            }
                        },
                        stringResource(R.string.learning_language),
                        {
                            Image(
                                painter = painterResource(R.drawable.outline_arrow_forward),
                                contentDescription = null)
                        },
                        onClick = {
                            navController.navigate("learningLanguage")
                            AnalyticsHelper.logEvent("language_change_menu")
                        })

                    SettingItems(
                        {
                            Image(
                                painter = painterResource(R.drawable.interfacelanguageicon),
                                contentDescription = null,
                                modifier = Modifier.size(80.dp))
                        },
                        stringResource(R.string.interface_language),
                        {
                            Image(
                                painter = painterResource(R.drawable.outline_arrow_forward),
                                contentDescription = null)
                        },
                        onClick = {

                           openAlertDialog.value = true


                        },
                        divider = true)

                    SettingItems(
                        icon = {
                            Image(
                                painter = painterResource(R.drawable.feedback),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(70.dp)
                                    .padding(start = 10.dp, top = 5.dp)
                            )
                        },
                        title = stringResource(R.string.give_feedback),
                        arrow= {},
                        onClick = {
                            viewModel.sendFeedback(context)

                        },
                        divider = false

                    )



                }

            }

            MyAppButton(
                modifier = Modifier,
                onClick = {
                    auth.signOut()
                    viewModel.clearUserPreferences(context)
                    navController.navigate("authentication") {
                        popUpTo("dashboard") { inclusive = true }

                        AnalyticsHelper.logEvent("logout_button")
                    }
                },
                text = stringResource(R.string.logout),
                colors= ButtonDefaults.buttonColors(
                    contentColor = White,
                    containerColor = Red
                )
            )

        }
    }
    if (infoModal){
    InfoModal(sheetState, onClick = { infoModal = false })}

    when{
        openAlertDialog.value->{
            AppSettingsAlertDialog(
                onConfirmation = { viewModel.openAppLanguages(context)
                                 openAlertDialog.value = false
                    AnalyticsHelper.logEvent("change_app_language")},
                onDismissRequest = {
                    openAlertDialog.value = false
                }
            )
        }
    }

}

