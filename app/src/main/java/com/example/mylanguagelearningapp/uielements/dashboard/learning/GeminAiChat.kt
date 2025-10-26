package com.example.mylanguagelearningapp.uielements.dashboard.learning

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylanguagelearningapp.ui.theme.White

@Composable
fun GeminAiChatUi(onClick: () -> Unit, width: Int, height: Int, state: ChatGPTState) {


    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .size(width.dp, height.dp)
            .background(White)
    ) {

        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 30.dp, end = 20.dp)
        ) {
            Button(
                onClick = {
                    onClick()
                }
            ) {
                Text(
                    text = "Close"
                )
            }

        }
        when (state) {

            is ChatGPTState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is ChatGPTState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 100.dp, start = 20.dp)
                ) {
                    item {
                        val result= (state as ChatGPTState.Success).response
                        Text(
                            text = result,
                            modifier = Modifier,
                            fontSize = 20.sp
                        )
                    }
                }
            }
            is ChatGPTState.Error -> {
                Text(
                    text = state.message,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

        }


    }
}