package com.example.mylanguagelearningapp.uielements.dashboard.home.mylist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylanguagelearningapp.ui.theme.BgBlue
import com.example.mylanguagelearningapp.ui.theme.Blue
import com.example.mylanguagelearningapp.ui.theme.White
import java.nio.file.WatchEvent

@Composable
fun WordCard(word: String,
             pronunciation: String,
             translation: String,
             isSelectable: Boolean = false,
             isSelected: Boolean = false,
             onClick: () -> Unit = {}) {

    Card(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .padding(10.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(White),
        border = BorderStroke(1.dp, Blue)) {

        Row(

        ){

        Column(modifier = Modifier.weight(1f)) {


            Text(
                text = word,
                modifier = Modifier
                    .padding(top= 10.dp, start = 20.dp),
                fontSize = 40.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                color = BgBlue,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis

            )

            Text(
                text = pronunciation,
                modifier = Modifier
                    .padding(top = 5.dp, start = 20.dp),
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                color = BgBlue
            )

            Text(
                text = translation,
                modifier = Modifier
                    .padding(top = 10.dp, start = 20.dp),
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif
            )

        }
            if (isSelectable){
            Checkbox(
                checked = isSelected,
                onCheckedChange = {onClick()},
                modifier = Modifier.padding(end = 20.dp)
            ) }
    }
    }



}

@Preview
@Composable
private fun Preview() {
    WordCard("こんにちは", "こんにちは", "Hello")
    
}
