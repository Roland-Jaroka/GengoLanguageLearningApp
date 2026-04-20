package com.example.gengolearning.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gengolearning.model.appmodels.WordCategories
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.JapaneseFontFamily
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
             currentLanguage: String,
             categories: List<WordCategories> = emptyList(),
             isLoading: Boolean = false,
             expandable: Boolean = true ) {

    var expanded by remember { mutableStateOf(false) }
    val collapsedHeight = 220.dp


if (!isLoading) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .combinedClickable(
                onClick = {
                    onTap()

                    if (expandable) {
                        expanded = !expanded
                    }
                },
                onLongClick = { longTap() },
                indication = LocalIndication.current,
                interactionSource = null
            ),
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(White)
    ) {

        Box {

            Column(
                modifier = Modifier
                    .heightIn(max = if (expanded) Dp.Unspecified else collapsedHeight)
                    .animateContentSize()
            ) {


                Text(
                    text = word,
                    modifier = Modifier
                        .padding(top = 10.dp, start = 20.dp),
                    fontSize = 40.sp,
                    fontFamily = JapaneseFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = BgBlue,
                    maxLines = if (expanded) Int.MAX_VALUE else 1,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 45.sp

                )
                if (currentLanguage == "jp" || currentLanguage == "cn") {

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
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, bottom = 5.dp)
                ) {

                   item {
                       categories.forEach { category ->
                           FilterChip(
                               selected = false,
                               onClick = {},
                               label = {
                                   Text(text = category.categoryName)
                               },
                               colors = if (category.color != null) FilterChipDefaults.filterChipColors(
                                   Color(category.color)
                               ) else FilterChipDefaults.filterChipColors(
                                   containerColor = Blue
                               ),
                               modifier = Modifier
                                   .padding(5.dp),
                               shape = RoundedCornerShape(20.dp),

                               )
                       }
                   }
                }

            }
            if (isSelectable) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onClick() },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 20.dp)

                )
            }
        }
    }
} else {
           Card(
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(10.dp)
                   .height(150.dp),
               elevation = CardDefaults.cardElevation(5.dp),
               colors = CardDefaults.cardColors(White)
           ) {
               Column {
                   Spacer(
                       modifier = Modifier
                           .height(10.dp)
                   )
                   Box(
                       modifier = Modifier
                           .fillMaxWidth(0.8f)
                           .height(30.dp)
                           .padding(top = 10.dp, start = 10.dp)
                           .shimmerEffect()
                   )

                   Spacer(
                       modifier = Modifier
                           .height(10.dp)
                   )
                   Box(
                       modifier = Modifier
                           .fillMaxWidth(0.5f)
                           .height(30.dp)
                           .padding(top = 10.dp, start = 10.dp)
                           .shimmerEffect()
                   )

                   Spacer(
                       modifier = Modifier
                           .height(10.dp)
                   )

                   Box(
                       modifier = Modifier
                           .fillMaxWidth(0.4f)
                           .height(30.dp)
                           .padding(top = 10.dp, start = 10.dp)
                           .shimmerEffect()
                   )
               }

           }




}



}

@Preview
@Composable
private fun Preview() {
    WordCard(
        word = "日本語",
        pronunciation = "pronunciation",
        translation = "translation",
        currentLanguage = "jp",
        isLoading = false
    )
}
