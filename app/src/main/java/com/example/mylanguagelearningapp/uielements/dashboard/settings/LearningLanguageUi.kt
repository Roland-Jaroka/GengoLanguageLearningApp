package com.example.mylanguagelearningapp.uielements.dashboard.settings

import android.provider.Settings
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAbsoluteAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mylanguagelearningapp.R
import com.example.mylanguagelearningapp.model.CountryFlags
import com.example.mylanguagelearningapp.ui.theme.BgBlue
import com.example.mylanguagelearningapp.ui.theme.Blue
import com.example.mylanguagelearningapp.ui.theme.LightBlue
import com.example.mylanguagelearningapp.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningLanguageUi(viewModel: LearningLanguageViewModel= viewModel(),
                       navController: NavController) {

    val currentLanguage by viewModel.language
    var selectedLanguage by remember { mutableStateOf(currentLanguage) }


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
                border = BorderStroke(2.dp, Blue)
            ) {
                Text(
                    text = "Select the language flag you want to learn",
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
                border = BorderStroke(2.dp, Blue)
            ) {
                Text(
                    text = "Select the main languge you want the app to start with",
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
        }
    }
}
