package com.example.gengolearning.uielements.dashboard.home.mainlanguage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gengolearning.model.AnalyticsHelper
import com.example.gengolearning.model.Languages
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.Pink
import com.example.gengolearning.ui.theme.TonedPink
import com.example.gengolearning.ui.theme.White
import com.example.gengolearning.uielements.uimodels.MyAppButton
import com.gengolearning.app.R

@Composable
fun MainLanguageSelector(navController: NavController,
                         viewModel: MainLanguageSelectorViewModel = hiltViewModel()) {
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
                text = stringResource(R.string.language_selector_header),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 30.sp
            )
            Text(
                text = "(${stringResource(R.string.language_selector_hint)})",
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
                             viewModel.setMainLanguage(selectedLanguage)
                                viewModel.setLanguage(selectedLanguage)
                                navController.navigate("dashboard") {
                                    popUpTo("mainLanguageSelector") {
                                        inclusive = true
                                    }
                                }
                        AnalyticsHelper.logEvent("selected_language_$selectedLanguage")
                    },
                    text = stringResource(R.string.select_button),
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
