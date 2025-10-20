package com.example.mylanguagelearningapp.uielements.dashboard.settings

import android.provider.ContactsContract
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.Scaffold
import com.example.mylanguagelearningapp.R
import com.example.mylanguagelearningapp.grammar.ChineseGrammar
import com.example.mylanguagelearningapp.grammar.JapaneseGrammar
import com.example.mylanguagelearningapp.grammar.LanguageGrammar
import com.example.mylanguagelearningapp.model.UserSettingsRepository
import com.example.mylanguagelearningapp.ui.theme.Blue
import com.example.mylanguagelearningapp.ui.theme.White
import com.example.mylanguagelearningapp.uielements.uimodels.MyTopAppBar
import com.example.mylanguagelearningapp.uielements.uimodels.ProfileTextFields
import com.example.mylanguagelearningapp.words.ChineseWords
import com.example.mylanguagelearningapp.words.JapaneseWords
import com.example.mylanguagelearningapp.words.LanguageWords
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileMenu(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val email= currentUser?.email.toString()
    val currentLanguage by UserSettingsRepository.language.collectAsState()
    val wordList by LanguageWords.words.collectAsState()
    val grammarList by LanguageGrammar.grammar.collectAsState()
    val profileName = UserSettingsRepository.profileName.value.toString()
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
                    .padding(start = 10.dp, end = 10.dp, top = 100.dp),
                    elevation = CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(White)) {
                    //Elements:
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
                            normalText = currentLanguage
                        )

                        ProfileTextFields(
                            boldText = "Number of words:",
                            normalText = wordList.size.toString()
                        )
                        ProfileTextFields(
                            boldText = "Number of grammar points:",
                            normalText = grammarList.size.toString()

                        )

                        ProfileTextFields(
                            boldText = "App version:",
                            normalText = appversion
                        )
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