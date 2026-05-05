package com.example.gengolearning.ui.features.dashboard.home.aiquiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gengolearning.ui.theme.White

@Composable
fun QuestionCard(question: String,
                 modifier: Modifier = Modifier) {

    val scrollState = rememberScrollState()

    Card(
     colors = CardDefaults.cardColors(
         White),
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp, max = 300.dp)
            .padding(horizontal = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
        SelectionContainer {
            Text(
                text = question,
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            )
        }

        }

    }
}

@Preview
@Composable
private fun Preview() {
    QuestionCard(
        question = "明日は土曜日です。昨日は木曜日でしたね。今日は何のひ"
    )
}