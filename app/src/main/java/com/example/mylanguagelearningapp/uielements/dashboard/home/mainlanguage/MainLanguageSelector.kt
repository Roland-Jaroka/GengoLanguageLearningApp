package com.example.mylanguagelearningapp.uielements.dashboard.home.mainlanguage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mylanguagelearningapp.model.UserSettingsRepository
import com.example.mylanguagelearningapp.ui.theme.Blue
import com.example.mylanguagelearningapp.ui.theme.White
import kotlinx.coroutines.launch
import com.example.mylanguagelearningapp.R
import com.example.mylanguagelearningapp.model.Languages
import com.example.mylanguagelearningapp.ui.theme.BgBlue
import com.example.mylanguagelearningapp.ui.theme.Pink
import com.example.mylanguagelearningapp.ui.theme.TonedPink
import com.example.mylanguagelearningapp.uielements.uimodels.MyAppButton

@Composable
fun MainLanguageSelector(navController: NavController) {
    val languages = Languages.languagesList
    var selectedLanguage by remember{mutableStateOf("")}
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var visible by remember { mutableStateOf(false) }



    LaunchedEffect(Unit) {
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ){ Column {
        Column(
            modifier = Modifier
                .padding(top = 150.dp, start = 10.dp, end = 10.dp, bottom = 20.dp)
        ) {
            Text(
                text = "Choose a language you want the app to start with",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 30.sp
            )
            Text(
                text = "(you can change it later)",
                fontSize = 20.sp
            )
        }


        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp, end = 10.dp)
                .fillMaxWidth()
        ) {
            languages.forEach { language ->
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(start = 30.dp, end = 30.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Pink),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(language.flag),
                            contentDescription = null,
                            modifier = Modifier
                                .size(70.dp)
                                .padding(start = 5.dp)

                        )
                        Text(
                            text = language.name,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(start = 20.dp)
                                .weight(1f)
                        )

                        Checkbox(
                            checked = selectedLanguage == language.code,
                            onCheckedChange = {
                                selectedLanguage = language.code
                            },
                            modifier = Modifier
                                .align(Alignment.CenterVertically),
                            colors = CheckboxDefaults.colors(
                                checkedColor = Pink,
                                uncheckedColor = TonedPink

                            )
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                    )
                }
            }

        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(150.dp))
    }

        AnimatedVisibility(
            visible = visible,
            modifier = Modifier
                .align(Alignment.BottomCenter),
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(durationMillis = 1000, delayMillis = 400)
            )
        )
            {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .background(BgBlue)
            )
            {
                MyAppButton(
                    onClick = {
                        if (selectedLanguage != "")
                            scope.launch {
                                UserSettingsRepository.setMainLanguage(context, selectedLanguage)
                                UserSettingsRepository.setLanguage(selectedLanguage)
                                navController.navigate("dashboard") {
                                    popUpTo("mainLanguageSelector") {
                                        inclusive = true
                                    }
                                }
                            }
                    },
                    text = "Select",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue,
                        contentColor = White
                    )
                )
            }
        }
    }
        }

@Preview
@Composable
private fun Preview() {
    MainLanguageSelector(navController = NavController(LocalContext.current))

}
