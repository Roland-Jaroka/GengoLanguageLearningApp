package com.example.gengolearning.uielements.uimodels

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.White

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WordCard(word: String,
             pronunciation: String,
             translation: String,
             isSelectable: Boolean = false,
             isSelected: Boolean = false,
             onClick: () -> Unit = {},
             longTap: () -> Unit = {},
             currentLanguage: String) {

    Card(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .padding(10.dp)
        .combinedClickable(
            onClick = {},
            onLongClick = { longTap() }
        ),
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
            if (currentLanguage== "jp" || currentLanguage== "cn") {

                Text(
                    text = pronunciation,
                    modifier = Modifier
                        .padding(top = 5.dp, start = 20.dp),
                    fontSize = 30.sp,
                    fontFamily = FontFamily.SansSerif,
                    color = BgBlue
                )
            }

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

