package com.example.gengolearning.uielements.dashboard.home.quiz

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.White

@Composable
fun NewQuizUi() {
    Box(modifier= Modifier.fillMaxSize()){

        Column(
            modifier= Modifier.fillMaxSize()) {
            LinearProgressIndicator(
                progress = { 0.5f },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)
                    .align(Alignment.CenterHorizontally),
                color = Blue
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(200.dp),
                colors = CardDefaults.cardColors(White),
                elevation = CardDefaults.cardElevation(20.dp)
            ) {
                Text(
                    text = "日本",
                    fontSize = 50.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp)
                )
                Text(text = "Japan",
                    fontSize = 40.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally))
            }


                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .height(400.dp),
                    colors = CardDefaults.cardColors(White),
                    elevation = CardDefaults.cardElevation(5.dp)
                ) {
                    Box(modifier= Modifier.fillMaxSize()){

                    }
                }



        }

    }

}

@Preview
@Composable
private fun Preview() {
    NewQuizUi()

}