package com.example.mylanguagelearningapp.uielements.dashboard.learning

import android.R
import android.provider.CalendarContract
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylanguagelearningapp.ui.theme.BgBlue
import com.example.mylanguagelearningapp.ui.theme.White

@Composable
fun GrammarCards(grammar: String, explanation: String, example: String, onClick: () -> Unit = {}) {

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .clickable{onClick()},
        colors = CardDefaults.cardColors(White),
        elevation = CardDefaults.cardElevation(5.dp)) {

        Column {
            Text(
                text = grammar,
                modifier = Modifier
                    .padding(top= 10.dp, start = 20.dp),
                fontSize = 40.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                color = BgBlue
            )
            Text(
                text = explanation,
                modifier = Modifier
                    .padding(top = 10.dp, start = 20.dp),
                fontSize = 25.sp,
                fontFamily = FontFamily.SansSerif
            )

            Text(
                text = example,
                modifier = Modifier
                    .padding(top = 10.dp, start = 20.dp, bottom = 10.dp),
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif
            )
        }


    }

    
}

@Preview
@Composable
private fun Preview() {
    GrammarCards(
        grammar= "~たりする",
        explanation = "Do this and that",
        example = "勉強したり、映画を見たりする",
        onClick = {}
    )

}