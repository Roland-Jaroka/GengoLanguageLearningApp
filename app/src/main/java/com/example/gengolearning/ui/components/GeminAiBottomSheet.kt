package com.example.gengolearning.ui.components


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.Inter
import com.example.gengolearning.ui.theme.White
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeminAiBottomSheet(close: () -> Unit = {},
                       isLoading: Boolean = false,
                       text: String = "") {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scrollState = rememberScrollState()

    var displayedText by remember { mutableStateOf("") }

    LaunchedEffect(text) {
        displayedText = ""
        for (char in text) {
            displayedText += char
            delay(10)
        }
    }


    ModalBottomSheet(
        onDismissRequest = {
            close()
        },
        sheetState = sheetState,
        containerColor = White

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            IconButton(
                onClick = {
                    close()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = BgBlue,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(5.dp)
                )
            }

            Column {

                if (!isLoading) {
                    Text(
                        text = displayedText,
                        fontFamily = Inter,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, top = 50.dp),

                        )
                }
                AnimatedVisibility(
                    visible = isLoading
                ) {

                    Column(
                        modifier = Modifier
                            .padding(top = 50.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .padding(start = 10.dp, end = 10.dp)
                                .height(20.dp)
                                .shimmerEffect()
                        )

                        repeat(10) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(start = 10.dp, end = 10.dp, top = 15.dp)
                                    .height(20.dp)
                                    .shimmerEffect()
                            )
                        }
                    }

                }
            }
        }

    }
}

@Preview
@Composable
private fun Preview() {
    GeminAiBottomSheet(
        isLoading = true
    )
}