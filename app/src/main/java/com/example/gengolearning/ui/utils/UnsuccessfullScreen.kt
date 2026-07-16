package com.example.gengolearning.ui.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.repeatCount
import com.example.gengolearning.ui.components.MyAppButton
import com.example.gengolearning.ui.features.dashboard.settings.Profile.EmailEditScreen.imageLoader
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R

@Composable
fun UnsuccessfullScreen(onDismiss: () -> Unit = {},
                        description: String) {

    val context = LocalContext.current
    val request = remember {
        ImageRequest.Builder(context)
            .data(R.drawable.unsuccess)
            .repeatCount(0)
            .build()
    }

    var descriptionVisibility by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {

        descriptionVisibility = true
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .pointerInput(Unit) {}
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
        ) {

            AsyncImage(
                model = request,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(100.dp),
                imageLoader = imageLoader(context)
            )
            AnimatedVisibility(
                visible = descriptionVisibility
            ) {

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    color = White,
                    shadowElevation = 10.dp,
                    shape = RoundedCornerShape(20.dp)

                ) {

                    Text(
                        text = description,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(10.dp)
                    )
                }
            }
        }

        MyAppButton(
            onClick = {
                onDismiss()
            },
            text = stringResource(R.string.okay_button),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        )
    }


}
