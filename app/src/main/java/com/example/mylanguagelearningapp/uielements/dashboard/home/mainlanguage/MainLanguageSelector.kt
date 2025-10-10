package com.example.mylanguagelearningapp.uielements.dashboard.home.mainlanguage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mylanguagelearningapp.model.UserSettingsRepository
import com.example.mylanguagelearningapp.ui.theme.Blue
import com.example.mylanguagelearningapp.ui.theme.White
import kotlinx.coroutines.launch
import com.example.mylanguagelearningapp.R

@Composable
fun MainLanguageSelector(navController: NavController) {
    val scope = rememberCoroutineScope()
    var language by remember { mutableStateOf("") }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(White))
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)

        ) {
            Card(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(300.dp)
                    .padding(top = 200.dp),
                colors = CardDefaults.cardColors(White),
                border = BorderStroke(2.dp, Blue),
                elevation = CardDefaults.cardElevation(5.dp)
            ) {
                Text(
                    text = "Select the language you want to learn",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(20.dp),
                    fontSize = 20.sp
                )

                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {

                    Image(
                        painter = painterResource(R.drawable.japanese),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )

                    Text(
                        text = "Japanese",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Checkbox(
                        checked = if (language == "jp") true else false,
                        onCheckedChange = { language= "jp"},
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 20.dp)
                    )
                }


                    HorizontalDivider(
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                    )

                    Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {

                        Image(
                            painter = painterResource(R.drawable.chinese),
                            contentDescription = null,
                            modifier = Modifier.size(50.dp)
                        )

                        Text(
                            text = "Chinese",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Checkbox(
                            checked = if (language == "cn") true else false,
                            onCheckedChange = { language = "cn" },
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 20.dp)
                        )
                    }

            }

        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 100.dp, start = 20.dp, end = 20.dp)
                .align(Alignment.BottomCenter),
            onClick = {
                if (language.isNotEmpty()) {
                    scope.launch {
                        UserSettingsRepository.setFirstLogin()
                        UserSettingsRepository.setMainLanguage(language)
                        UserSettingsRepository.getUserData()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue,
                contentColor = White
            ),
            elevation = ButtonDefaults.buttonElevation(10.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "Start"
            )
        }
    }

        }
