package com.example.gengolearning.ui.components.global

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gengolearning.ui.theme.Blue
import com.gengolearning.app.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun OpeningLoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Image(
            painter = painterResource(R.drawable.main_logo),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.Center)
        )
    }
}

@Preview
@Composable
private fun Preview() {
    OpeningLoadingScreen()
}