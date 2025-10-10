package com.example.mylanguagelearningapp.uielements.dashboard.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mylanguagelearningapp.R
import com.example.mylanguagelearningapp.model.UserSettingsRepository
import com.example.mylanguagelearningapp.ui.theme.Blue
import com.example.mylanguagelearningapp.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningLanguageUi(viewModel: LearningLanguageViewModel= viewModel(),
                       navController: NavController) {

    val currentLanguage by viewModel.language
    val mainLanguage by UserSettingsRepository.mainLanguage
    var selectedMainLanguage by remember{ mutableStateOf(mainLanguage)}
    var selectedLanguage by remember { mutableStateOf(currentLanguage)}


    Scaffold( modifier = Modifier
        .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("Select Language")
                },
                navigationIcon = {
                    IconButton({
                        navController.navigate("settings"){
                            popUpTo("settings") { inclusive = true }
                        }
                    }) {
                        Image(
                            painter = painterResource(R.drawable.arrow_back2),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(White)

            )
        }

        ) { innerPadding ->
        Column (modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(White)) {
            Card(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(300.dp)
                    .padding(12.dp),
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

                Row(modifier = Modifier.align(Alignment.CenterHorizontally)){

                    Text(
                        text = "Japanese",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Checkbox(
                        checked = selectedLanguage == "jp",
                        onCheckedChange = {
                           if (it) selectedLanguage = "jp"
                        },
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 20.dp)
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                )

                Row(modifier = Modifier.align(Alignment.CenterHorizontally)){

                    Text(
                        text = "Chinese",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Checkbox(
                        checked = selectedLanguage == "cn",
                        onCheckedChange = {if (it) selectedLanguage = "cn"},
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 30.dp)
                    )
                }


                Button(
                    onClick = {
                        viewModel.setLanguage(selectedLanguage)
                        navController.navigate("home") {
                            popUpTo(navController.graph.startDestinationId) { saveState = false }
                            launchSingleTop = true
                            restoreState = true

                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                        .padding(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue,
                        contentColor = White
                    ),
                    elevation = ButtonDefaults.buttonElevation(10.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Select"
                    )
                }

            }

            Card(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(300.dp)
                    .padding(12.dp),
                colors = CardDefaults.cardColors(White),
                border = BorderStroke(2.dp, Blue),
                elevation = CardDefaults.cardElevation(5.dp)
            ) {
                Text(
                    text = "Select the main language you want the app to start with",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(20.dp),
                    fontSize = 20.sp
                )

                Row(modifier = Modifier.align(Alignment.CenterHorizontally)){

                    Text(
                        text = "Japanese",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Checkbox(
                        checked = selectedMainLanguage == "jp",
                        onCheckedChange = {
                            if (it) selectedMainLanguage = "jp"
                        },
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 20.dp)
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                )

                Row(modifier = Modifier.align(Alignment.CenterHorizontally)){

                    Text(
                        text = "Chinese",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Checkbox(
                        checked = selectedMainLanguage == "cn",
                        onCheckedChange = {if (it) selectedMainLanguage = "cn"},
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 30.dp)
                    )
                }


                Button(
                    onClick = {
                        viewModel.setMainLanguage(selectedMainLanguage)
                        navController.navigate("settings") {
                            popUpTo(navController.graph.startDestinationId) { saveState = false }
                            launchSingleTop = true
                            restoreState = true

                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                        .padding(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue,
                        contentColor = White
                    ),
                    elevation = ButtonDefaults.buttonElevation(10.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Select"
                    )
                }

            }
        }
    }
}
