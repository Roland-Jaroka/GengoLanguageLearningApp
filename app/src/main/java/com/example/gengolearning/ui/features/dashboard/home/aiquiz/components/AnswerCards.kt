package com.example.gengolearning.ui.features.dashboard.home.aiquiz.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gengolearning.ui.theme.LeafGreen
import com.example.gengolearning.ui.theme.Red
import com.example.gengolearning.ui.theme.White

@Composable
fun AnswerCard(modifier: Modifier = Modifier,
               answer: String,
               isCorrect: Boolean = false,
               isFalse: Boolean = false,
               onClick: () -> Unit = {}) {
    Card(
        colors = CardDefaults.cardColors(White),
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp)
            .padding(horizontal = 10.dp)
            .clickable{
                onClick()
            },
        border = if (isCorrect) BorderStroke(
            1.dp, LeafGreen)
        else if (isFalse) BorderStroke(
            1.dp, Red
        ) else null
    ) {
        Column(
            modifier= Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = answer,
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                overflow = TextOverflow.Clip
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AnswerCard(
        answer = "Text"
    )
}