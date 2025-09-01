package com.example.mylanguagelearningapp.uielements.dashboard.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.mylanguagelearningapp.R
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mylanguagelearningapp.ui.theme.BgBlue
import com.example.mylanguagelearningapp.ui.theme.Blue
import com.example.mylanguagelearningapp.ui.theme.White
import androidx.compose.material3.Text
import com.example.mylanguagelearningapp.model.CountryFlags
import com.example.mylanguagelearningapp.ui.theme.LightBlue
import com.example.mylanguagelearningapp.ui.theme.Purple40
import com.example.mylanguagelearningapp.ui.theme.PurpleGrey40
import com.example.mylanguagelearningapp.ui.theme.PurpleGrey80
import com.example.mylanguagelearningapp.ui.theme.WarmGrey

@Composable
fun settingsUi(navController: NavController) {
   //TODO settings UI and functions
    val auth = FirebaseAuth.getInstance()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(White)) {

        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center){

            Card(modifier = Modifier
                .padding(start = 12.dp, end = 12.dp),
                border = BorderStroke(2.dp, Blue),
                colors = CardDefaults.cardColors(LightBlue)){
                Column() {

                    SettingItems(
                        {
                            Image(
                                painter = painterResource(CountryFlags.JAPANESE.resID),
                                contentDescription = null,
                                modifier = Modifier.size(80.dp))
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
                        })


                }

            }


            Button( modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
                colors= ButtonDefaults.buttonColors(White, BgBlue),
                border = BorderStroke(2.dp,Blue),
                shape = RoundedCornerShape(12.dp),
            onClick = { auth.signOut()
                navController.navigate("authentication")
            }) {
                Text(text = "Logout")
        }

        }
    }

}

