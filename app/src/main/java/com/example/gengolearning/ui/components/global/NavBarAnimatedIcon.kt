package com.example.gengolearning.ui.components.global

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun NavBarAnimatedIcon(itemPath: Int, isSelected: Boolean) {

    val animateable = rememberLottieAnimatable()


    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(itemPath)
    )


    LaunchedEffect(isSelected, composition) {
        if (composition == null) return@LaunchedEffect

        if (isSelected){
            animateable.snapTo(progress = 0f)

            animateable.animate(
                composition = composition,
                iterations = 1,
                speed = 2f
            )


        } else {
            animateable.snapTo(progress = 0f)
        }
    }

    LottieAnimation(
        composition = composition,
        progress = {animateable.progress},
        modifier = Modifier
            .size(30.dp)
            .scale(3f)
    )

}