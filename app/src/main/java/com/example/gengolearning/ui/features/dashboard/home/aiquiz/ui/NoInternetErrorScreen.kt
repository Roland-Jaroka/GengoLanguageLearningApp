package com.example.gengolearning.ui.features.dashboard.home.aiquiz.ui

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.HapticFeedbackConstantsCompat
import androidx.credentials.exceptions.domerrors.InvalidModificationError
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.gengolearning.ui.components.MyAppButton
import com.example.gengolearning.ui.components.TextButton
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.Inter
import com.gengolearning.app.R

@Composable
fun NoInternetErrorScreen(
    onTryAgain: () -> Unit = {},
    onBack: () -> Unit = {}
) {
  val composition by rememberLottieComposition(
      LottieCompositionSpec.RawRes(R.raw.no_internet)
  )

val progress by animateLottieCompositionAsState(
    composition = composition,
    iterations = 1
)

    var isFinished by remember { mutableStateOf(false) }

    val haptic = LocalHapticFeedback.current

    LaunchedEffect(progress) {
         if (progress >= 1f) {
             isFinished = true
         }
    }

    LaunchedEffect(Unit) {
           haptic.performHapticFeedback(
               HapticFeedbackType.Reject
           )
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier.weight(1f)
        )

        LottieAnimation(
            composition =composition,
            progress = {
                if (isFinished) 1f else progress},
            modifier = Modifier
                .size(200.dp)
        )
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )

        Text(
            text = stringResource(R.string.no_internet_title),
            modifier = Modifier
                .padding(bottom = 20.dp),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Inter
        )

        Text(
            text = stringResource(R.string.no_internet),
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
            text = stringResource(R.string.common_try_again),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue
            )

        )

        TextButton(
            onClick = {
                onBack()
            },
            text = stringResource(R.string.back_button),
            modifier = Modifier
                .navigationBarsPadding()
        )
    }
}

@Preview
@Composable
private fun Preview() {
    NoInternetErrorScreen()
}