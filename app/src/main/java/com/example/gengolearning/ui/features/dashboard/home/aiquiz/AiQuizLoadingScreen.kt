package com.example.gengolearning.ui.features.dashboard.home.aiquiz

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.gengolearning.ui.theme.TransParentBackground
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R
import kotlinx.coroutines.delay

@Composable
fun AiQuizLoadingScreen() {
      val composition by rememberLottieComposition(
          LottieCompositionSpec.RawRes(R.raw.learning)
      )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    val tips = remember { mutableStateListOf(
        R.string.loading_tip1,
        R.string.loading_tip2,
        R.string.loading_tip3,
        R.string.loading_tip4
    ).shuffled() }

    var currentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(currentIndex) {
        delay(5000)

        currentIndex = (currentIndex + 1) % tips.size
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TransParentBackground)
            .pointerInput(Unit){
                detectTapGestures {
                    currentIndex = (currentIndex + 1) % tips.size
                }
            }

    ) {
        Column ( modifier = Modifier
            .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .size(400.dp)

            )

                AnimatedContent(
                    targetState = currentIndex,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(800)) togetherWith fadeOut(animationSpec = tween(800))
                    },
                    label = "tipAnimation"
                ) { index ->
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(start = 5.dp, end = 5.dp)
                    ) {

                    Image(
                        painter = painterResource(R.drawable.tips),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .size(30.dp)

                    )
                    Text(
                        text = stringResource(tips[index]),
                        fontSize = 15.sp,
                        color = White
                    )
                }
                }

        }

    }
}

@Preview
@Composable
private fun Preview() {
    AiQuizLoadingScreen()
}