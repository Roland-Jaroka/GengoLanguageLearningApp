package com.example.gengolearning.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.White

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WordCard(word: String,
             pronunciation: String,
             translation: String,
             isSelectable: Boolean = false,
             isSelected: Boolean = false,
             onClick: () -> Unit = {},
             onTap: () -> Unit = {},
             longTap: () -> Unit = {},
             currentLanguage: String) {

    var expanded by remember { mutableStateOf(false) }
    val collapsedHeight = 200.dp



    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .combinedClickable(
            onClick = {
                onTap()
                expanded = !expanded
            },
            onLongClick = { longTap() },
            indication = LocalIndication.current,
            interactionSource = null
        )
        ,
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(White)) {

        Row{

        Column(modifier = Modifier
            .weight(1f)
            .heightIn(max= if (expanded) Dp.Unspecified else collapsedHeight)
            .animateContentSize()
        ) {


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
                fontFamily = FontFamily.SansSerif,
                lineHeight = 45.sp,
                maxLines = if (expanded) Int.MAX_VALUE else 1,
                overflow = TextOverflow.Ellipsis
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

