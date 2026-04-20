package com.example.gengolearning.ui.components


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gengolearning.model.appmodels.WordCategories
import com.gengolearning.app.R

@Composable
fun CategoryListCard(category: WordCategories,
                     number: Int,
                     onClick: () -> Unit = {},
                     onLongClick: () -> Unit = {},
                     showWords: Boolean = false,
                     isEditMode: Boolean = false,
                     isSelected: Boolean = false,
                     onCheckedChange: () -> Unit = {},
                     onCategoryDelete: () -> Unit = {}
                     ) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(10.dp)
            .combinedClickable(
                onClick = {
                    onClick()
                },
                onLongClick = {
                    onLongClick()
                }
            ),
        elevation = CardDefaults.cardElevation(5.dp),
        colors = category.color?.let {
            CardDefaults.cardColors(Color(it))
        } ?: CardDefaults.cardColors()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = category.categoryName,
                    modifier = Modifier
                        .padding(10.dp),
                    fontSize = 30.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis

                )

                Text(
                    text = stringResource(R.string.category_cards_numer_words, "$number"),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 5.dp)
                )

                Icon(
                    imageVector = if (!showWords) Icons.Default.KeyboardArrowDown
                    else Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .align(Alignment.End)
                )

            }

            if (isEditMode) {

                Checkbox(
                    checked = isSelected ,
                    onCheckedChange = {
                          onCheckedChange()
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 2.dp, end = 5.dp),
                )

            }
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 2.dp, start = 2.dp)
            ) {
                AnimatedVisibility(
                    visible = isEditMode
                ) {

                    IconButton(
                        onClick = {
                            onCategoryDelete()
                        },

                    ) {
                        Image(
                            painter = painterResource(R.drawable.delete_icon_mini),
                            contentDescription = null,

                            )
                    }
                }
            }

        }

    }
}

@Preview
@Composable
private fun Preview() {

    val categories = WordCategories(categoryName = "Category 1")
    CategoryListCard(
        categories,
        number = 12,
        isEditMode = true,
        showWords = true
        )
}