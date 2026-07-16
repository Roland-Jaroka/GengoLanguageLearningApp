package com.example.gengolearning.ui.features.dashboard.home.aiquiz.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.gengolearning.ui.components.MyAppButton
import com.example.gengolearning.ui.components.TextButton
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.Inter
import com.gengolearning.app.R

@Composable
fun AiQuizErrorScreen(
    onTryAgain: () -> Unit = {},
    onBack: () -> Unit = {},
    title: String,
    text: String,
    buttonText: String,
    backButtonText: String
) {

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.alert)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    val haptic = LocalHapticFeedback.current

    LaunchedEffect(Unit) {
        haptic.performHapticFeedback(
            HapticFeedbackType.Reject
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(
            modifier = Modifier
                .weight(1f)
        )

        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .size(200.dp)
        )

        Spacer(
            modifier = Modifier
                .height(20.dp)
        )

        Text(
            text = title,
            modifier = Modifier
                .padding(bottom = 20.dp),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Inter
        )

        Text(
            text = text,
            fontSize = 20.sp,
            fontFamily = Inter,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
        )

        Spacer(
            modifier = Modifier
                .weight(1f)
        )

        MyAppButton(
            onClick = {
                onTryAgain()
            },
            text = buttonText,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )

        )

        TextButton(
            onClick = {
                onBack()
            },
            text = backButtonText,
            modifier = Modifier
                .navigationBarsPadding()
        )
    }
}

@Preview
@Composable
private fun Preview() {
    AiQuizErrorScreen(
        title = "oops",
        text = "It seems like something went wrong, try again",
        buttonText = "Try again",
        backButtonText = "Go back"
    )
}