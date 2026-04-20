package com.example.gengolearning.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.gengolearning.ui.theme.TransParentBackground
import com.example.gengolearning.ui.theme.White

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TransParentBackground)
            .pointerInput(Unit) {}

    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .size(100.dp),
            color = White
        )
    }
}