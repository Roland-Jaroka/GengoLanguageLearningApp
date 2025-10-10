package com.example.mylanguagelearningapp.uielements.dashboard.settings

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mylanguagelearningapp.R
import com.example.mylanguagelearningapp.model.CountryFlags
import com.example.mylanguagelearningapp.model.UserSettingsRepository
import com.example.mylanguagelearningapp.ui.theme.BgBlue
import com.example.mylanguagelearningapp.ui.theme.Blue
import com.example.mylanguagelearningapp.ui.theme.LightBlue
import com.example.mylanguagelearningapp.ui.theme.Red
import com.example.mylanguagelearningapp.ui.theme.White
import com.example.mylanguagelearningapp.uielements.uimodels.MyAppButton
import com.google.firebase.auth.FirebaseAuth

@Composable
fun settingsUi(navController: NavController) {
   //TODO settings UI and functions
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val currentLanguage = UserSettingsRepository.language.value

    LaunchedEffect(Unit) {
        println("Language set to: ${UserSettingsRepository.language.value}")
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(White)) {

        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center){

            Card(modifier = Modifier
                .padding(start = 12.dp, end = 12.dp),
                colors = CardDefaults.cardColors(White),
                elevation = CardDefaults.cardElevation(10.dp)){
                Column() {

                    SettingItems(
                        icon = {
                            Image(
                                painter= painterResource(R.drawable.profile),
                                contentDescription = "profile",
                                modifier = Modifier.size(80.dp)
                            )
                        },
                        title = "Profile",
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
                                .border(1.dp, BgBlue, RoundedCornerShape(10.dp))
                                ){
                                Image(
                                    painter = painterResource(
                                        if (currentLanguage == "jp") CountryFlags.JAPANESE.resID else CountryFlags.CHINESE.resID
                                    ),
                                    contentDescription = null,
                                )
                            }
                        },
                        "Learning Language",
                        {
                            Image(
                                painter = painterResource(R.drawable.outline_arrow_forward),
                                contentDescription = null)
                        },
                        onClick = {
                            navController.navigate("learningLanguage")
                        })

                    SettingItems(
                        {
                            Image(
                                painter = painterResource(R.drawable.interfacelanguageicon),
                                contentDescription = null,
                                modifier = Modifier.size(80.dp))
                        },
                        "Interface Language",
                        {
                            Image(
                                painter = painterResource(R.drawable.outline_arrow_forward),
                                contentDescription = null)
                        },
                        onClick = {
                            //TODO interface language
                            Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show()
                        },
                        divider = false)



                }

            }

            MyAppButton(
                modifier = Modifier,
                onClick = {
                    auth.signOut()
                    navController.navigate("authentication")
                },
                text = "Logout",
                colors= ButtonDefaults.buttonColors(
                    contentColor = White,
                    containerColor = Red
                )
            )

        }
    }

}

