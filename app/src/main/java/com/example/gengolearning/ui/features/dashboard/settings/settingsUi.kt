package com.example.gengolearning.ui.features.dashboard.settings

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.gengolearning.model.appmodels.Languages
import com.example.gengolearning.model.utils.AnalyticsHelper
import com.example.gengolearning.ui.components.LogoutDialog
import com.example.gengolearning.ui.components.MyAppButton
import com.example.gengolearning.ui.features.navigation.Route
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.Red
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun settingsUi(navController: NavController, viewModel: SettingsViewModel= hiltViewModel()) {
   //TODO settings UI and functions
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val currentLanguage by viewModel.currentLanguage.collectAsState(
        Languages.languagesList[0]
    )
    var logoutDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by rememberSaveable { mutableStateOf(false) }

    val image by viewModel.profileImage.collectAsState()


    Column(modifier = Modifier
        .fillMaxSize()
        .background(BgBlue)
        .statusBarsPadding()) {


            Text( text = stringResource(R.string.setting_button),
                fontSize = 50.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.Bold,
                color = White)

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = White,
            shape = RoundedCornerShape(topStart = 90.dp),
            shadowElevation = 10.dp
        ) {


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
                                painter= when (val state = image) {
                                    null -> painterResource(R.drawable.profile)
                                    else ->  {
                                        rememberAsyncImagePainter(state.image)
                                    }

                                },
                                contentDescription = "profile",
                                modifier = Modifier
                                    .size(80.dp)
                                    .padding(top = 5.dp, bottom = 5.dp)
                                    .clip(RoundedCornerShape(30.dp))



                            )
                        },
                        title =stringResource(R.string.profile),
                        arrow = {
                            Image(
                                painter = painterResource(R.drawable.outline_arrow_forward),
                                contentDescription = "arrow")
                        },
                        onClick= {
                            navController.navigate(Route.Profile)
                        }

                    )

                    SettingItems(
                        {
                            Box(modifier = Modifier
                                .padding(top = 10.dp, start = 10.dp, bottom = 10.dp, end = 5.dp)
                                .background(White)
                                .height(65.dp)
                                .clip(RoundedCornerShape(10.dp))
                                )
                                {
                                Image(
                                    painter = painterResource(currentLanguage.flag),
                                    contentDescription = null,
                                    modifier = if (currentLanguage.code == "jp") {
                                        Modifier
                                            .width(70.dp)
                                            .height(60.dp)
                                            .border(0.5.dp, BgBlue, shape = RoundedCornerShape(10.dp))
                                        }
                                        else Modifier
                                        .size(70.dp)
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
                            navController.navigate(Route.LearningLanguage)
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

//                           openAlertDialog.value = true
                            showLanguageDialog = true


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
                    logoutDialog = true
                },
                text = stringResource(R.string.logout),
                colors= ButtonDefaults.buttonColors(
                    contentColor = White,
                    containerColor = Red
                )
            )

        }
    }


    if (logoutDialog) {
        LogoutDialog(
            onDismiss = {
                logoutDialog = false
            },
            onConfirm = {
                auth.signOut()
                viewModel.clearUserPreferences(context)
                logoutDialog = false
                navController.navigate(Route.Authentication) {
                    popUpTo(Route.Dashboard) { inclusive = true }

                    AnalyticsHelper.logEvent("logout_button")
                }
            },
            title = stringResource(R.string.logout_title),
            body = stringResource(R.string.logout_body),
            confirmButtonText = stringResource(R.string.logout),
            dismissButtonText = stringResource(R.string.cancel)

        )
    }

    if (showLanguageDialog) {
        AppLanguageSelectorBottomSheet(
            onDismiss = { showLanguageDialog = false }
        )
    }

    }

}

