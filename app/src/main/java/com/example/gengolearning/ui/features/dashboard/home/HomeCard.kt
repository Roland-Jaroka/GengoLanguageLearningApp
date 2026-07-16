package com.example.gengolearning.ui.features.dashboard.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gengolearning.model.appmodels.Language
import com.example.gengolearning.model.appmodels.Languages
import com.example.gengolearning.model.appmodels.Words
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.JapaneseFontFamily
import com.example.gengolearning.ui.theme.MyLanguageLearningAppTheme
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R
import kotlin.math.abs

@Composable
fun HomeCard(currentIndex: Int,
             wordListSize: Int,
             onNextClick: () ->  Unit,
             onPreviousClick: () -> Unit,
             currentLanguage: Language,
             isWordVisible: Boolean = true,
             isTranslationVisible: Boolean = true,
             isPronunciationVisible: Boolean = true,
             currentWord: Words?
             ) {

    var accumulated by remember { mutableFloatStateOf(0f) }

    Column(modifier = Modifier.fillMaxWidth()) {

        Text(
            text = "${currentIndex + 1}/${wordListSize}",
            modifier = Modifier
                .padding(end = 30.dp, top = 12.dp)
                .align(Alignment.End),
            fontSize = 20.sp,
            fontFamily = FontFamily.SansSerif
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(start = 12.dp, end = 12.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(onHorizontalDrag = { change, dragAmount ->

                        accumulated += dragAmount
                        change.consume()
                        val treshold = 200f
                        if (abs(accumulated) > treshold) {
                            if (accumulated > 0) {
                                onNextClick()


                            } else {
                                onPreviousClick()

                            }
                            accumulated = 0f
                        }
                    })
                },
            elevation = CardDefaults.cardElevation(10.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
        ) {

            Column(modifier = Modifier.fillMaxSize()) {

                Row(
                    modifier = Modifier
                        .height(100.dp)
                        .background(MaterialTheme.colorScheme.primary)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(R.drawable.outline_arrow_back),
                        contentDescription = null,
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(start = 10.dp)
                            .size(25.dp)
                            .clickable {
                                onPreviousClick()

                            }
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .weight(4f)
                    )
                    {
                        Image(
                            painter = painterResource(currentLanguage.flag),
                            contentDescription = null,
                            modifier = Modifier
                                .size(90.dp)
                                .padding(end = 5.dp),
                        )

                        Text(
                            text = stringResource(R.string.word_card_todays_word),
                            color = White,
                            fontSize = 20.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }

                    Image(
                        painter = painterResource(R.drawable.outline_arrow_forward),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .align(Alignment.CenterVertically)
                            .size(25.dp)
                            .weight(0.5f)
                            .clickable {

                                onNextClick()

                            })
                }

                Text(
                    text = when {
                        !isWordVisible -> ""
                        currentWord != null -> currentWord.word
                        else -> ""
                    },
                    modifier = Modifier
                        .padding(top = 20.dp, start = 20.dp),
                    fontSize = 30.sp,
                    fontFamily = JapaneseFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                if (currentLanguage.code == "jp" || currentLanguage.code == "cn") {
                    Text(
                        text = when {
                            !isPronunciationVisible -> ""
                            currentWord != null -> currentWord.pronunciation
                            else -> ""
                        },
                        modifier = Modifier
                            .padding(top = 5.dp, start = 20.dp),
                        fontSize = 20.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Text(
                    text = when {
                        !isTranslationVisible -> ""
                        currentWord != null -> currentWord.translation
                        else -> ""
                    },
                    modifier = Modifier
                        .padding(top = 10.dp, start = 20.dp),
                    fontSize = 20.sp,
                    fontFamily = FontFamily.SansSerif
                )


            }

        }
    }
}

@Preview
@Composable
private fun Preview() {
    MyLanguageLearningAppTheme {
        HomeCard(
            currentIndex = 1,
            wordListSize = 5,
            onNextClick = {},
            onPreviousClick = {},
            currentLanguage = Languages.languagesList[0],
            currentWord = Words(
                "Test",
                "Test",
                "Test"
            )
        )
    }
}