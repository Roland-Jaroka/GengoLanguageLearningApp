package com.example.gengolearning.uielements.dashboard.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gengolearning.model.Languages
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.White
import com.example.gengolearning.uielements.uimodels.MyTopAppBar
import com.example.gengolearning.uielements.uimodels.ProfileTextFields
import com.gengolearning.app.R
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileMenu(navController: NavController, viewModel: ProfileViewModel = hiltViewModel()){
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val state by viewModel.profileState.collectAsState()
    val email= currentUser?.email.toString()
    val wordCount = viewModel.wordCount
    val languageCount = viewModel.languageCount
    val currentLanguage by viewModel.currentLanguage.collectAsState(
        Languages.languagesList[0]
    )
    val wordList by viewModel.wordsList.collectAsState()
    val grammarList by viewModel.grammar.collectAsState()
    val profileName = viewModel.profileName.toString()
    val appversion = LocalContext.current.packageManager.getPackageInfo(LocalContext.current.packageName, 0).versionName.toString()
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar= {MyTopAppBar(modifier = Modifier,
            title= "Profile",
            route= "settings",
            navController = navController)}
    ){ innerPadding->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            Box(modifier = Modifier.align(Alignment.TopCenter)){

                Card(modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .padding(start = 10.dp, end = 10.dp, top = 100.dp),
                    elevation = CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(White)) {
                    //Elements:
                        Box(modifier = Modifier.fillMaxSize()) {
                            if (state is ProfileState.Normal) {
                            Column(modifier = Modifier.padding(top = 50.dp, start = 50.dp)) {
                                ProfileTextFields(
                                    boldText = "Profile name:",
                                    normalText = profileName
                                )
                                ProfileTextFields(
                                    boldText = "Email:",
                                    normalText = email
                                )
                                ProfileTextFields(
                                    boldText = "Language:",
                                    normalText = currentLanguage.name
                                )

                                ProfileTextFields(
                                    boldText = "Number of ${currentLanguage.name} words:",
                                    normalText = wordList.size.toString()
                                )

                                ProfileTextFields(
                                    boldText = "Number of grammar points:",
                                    normalText = grammarList.size.toString()

                                )

                                ProfileTextFields(
                                    boldText = "Number of all words:",
                                    normalText = wordCount.toString()
                                )

                                ProfileTextFields(
                                    boldText = "Languages:",
                                    normalText = languageCount.toString()
                                )

                                ProfileTextFields(
                                    boldText = "App version:",
                                    normalText = appversion
                                )
                            }
                        } else {
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .background(White)
                        .clip(RoundedCornerShape(30.dp))
                        .border(2.dp, Blue, RoundedCornerShape(30.dp))
                ) {
                    Image(
                        painter = painterResource(R.drawable.profile),
                        contentDescription = "profile",
                        modifier = Modifier.size(130.dp)
                    )
                }
        }
        }
    }
}